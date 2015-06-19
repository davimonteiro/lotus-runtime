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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentContext;
import br.com.davimonteiro.lotus_runtime.checker.ModelCheckerComponent;
import br.com.davimonteiro.lotus_runtime.project.ProjectUtilComponent;

@Slf4j
public class TraceWatcherComponent implements Component {
	
	@Getter
	private Path traceFile;
	
	private ProjectUtilComponent projectUtilComponent;
	
	private ModelCheckerComponent modelCheckerComponent;
	
	private ProbabilisticAnnotator annotator;

	private TraceWatcherHelper traceWatcherHelper;

	private Long milliseconds;
	

	public TraceWatcherComponent(Path traceFile, Long milliseconds) {
		this.annotator = new ProbabilisticAnnotator();
		this.traceFile = traceFile;
		this.milliseconds = milliseconds;
	}

	@Override
	public void start(ComponentContext context) throws Exception {
		log.info("Starting the TraceWatcherComponent");
		
		this.projectUtilComponent = context.getComponent(ProjectUtilComponent.class);
		this.modelCheckerComponent = context.getComponent(ModelCheckerComponent.class);
		this.traceWatcherHelper = new TraceWatcherHelper(this, milliseconds);
		
		new Thread(traceWatcherHelper).start();
	}

	@Override
	public void stop(ComponentContext context) throws Exception {
		traceWatcherHelper.stop();
	}
	
	// Update the model and the project file
	public void updateModel(String[] trace) {
		this.annotator.annotate(projectUtilComponent.getComponent(), trace);
		this.projectUtilComponent.updateProject(projectUtilComponent.getProject());
		this.modelCheckerComponent.verifyModel();
	}

}


