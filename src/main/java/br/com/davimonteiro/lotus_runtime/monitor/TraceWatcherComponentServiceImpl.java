/**
 * The MIT License
 * Copyright (c) 2015 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.davimonteiro.lotus_runtime.monitor;

import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;
import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.checker.ModelCheckerComponentService;
import br.com.davimonteiro.lotus_runtime.project.ProjectUtilComponentService;

@Slf4j
public class TraceWatcherComponentServiceImpl implements Component, TraceWatcherComponentService {
	
	private Path traceFile;
	
	private ProjectUtilComponentService projectUtilComponentService;
	
	private ModelCheckerComponentService modelCheckerComponentService;
	
	private ProbabilisticAnnotator annotator;

	private TraceWatcherHelper traceWatcherHelper;

	private Long milliseconds;
	

	public TraceWatcherComponentServiceImpl(Path traceFile, Long milliseconds) {
		this.annotator = new ProbabilisticAnnotator();
		this.traceFile = traceFile;
		this.milliseconds = milliseconds;
	}

	@Override
	public void start(ComponentManager manager) throws Exception {
		log.info("Starting the TraceWatcherComponent");
		
		this.projectUtilComponentService = manager.getComponentService(ProjectUtilComponentService.class);
		this.modelCheckerComponentService = manager.getComponentService(ModelCheckerComponentService.class);
		this.traceWatcherHelper = new TraceWatcherHelper(this, milliseconds);
		
		new Thread(traceWatcherHelper).start();
	}

	@Override
	public void stop(ComponentManager manager) throws Exception {
		traceWatcherHelper.stop();
		manager.uninstallComponent(this);
	}
	
	// Update the model and the project file
	@Override
	public void updateModel(String[] trace) {
		this.annotator.annotate(projectUtilComponentService.getComponent(), trace);
		this.projectUtilComponentService.updateProject(projectUtilComponentService.getProject());
		this.modelCheckerComponentService.verifyModel();
	}

	@Override
	public Path getTraceFile() {
		return this.traceFile;
	}

}


