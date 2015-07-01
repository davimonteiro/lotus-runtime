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
package br.com.davimonteiro.lotus_runtime.project;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.model.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.LotusProject;

@Slf4j
public class ProjectUtilComponentServiceImpl implements Component, ProjectUtilComponentService {

	private Path projectFile;
	
	private LotusProject project;
	
	private ProjectSerializer serializer;
	
	
	public ProjectUtilComponentServiceImpl(Path projectFile) {
		this.projectFile = projectFile;
		this.serializer = new ProjectXMLSerializer();
	}

	@Override
	public void start(ComponentManager manager) throws Exception {
		loadProject();
	}
	
	@Override
	public void stop(ComponentManager manager) throws Exception {
		saveProject();
		manager.uninstallComponent(this);
	}

	private void saveProject() throws IOException, Exception {
		OutputStream outputStream = Files.newOutputStream(projectFile);
		serializer.toStream(project, outputStream);
	}
	
	private void loadProject() {
		try (InputStream in = Files.newInputStream(projectFile)) {
			project = serializer.parseStream(in);
			project.setValue("file", projectFile.toFile());
			
			if (isNullOrEmpty(project.getName())) {
				project.setName("Untitled");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO Throw an exception
			e.printStackTrace();
		}
		
	}
	
	@Override
	@Synchronized
	public void updateProject(LotusProject project) {
		this.project = project;

		try {
			saveProject();
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO Throw an exception
			e.printStackTrace();
		}
	}
	
	@Override
	@Synchronized
	public LotusProject getProject() {
		return this.project;
	}
	
	@Override
	@Synchronized
	public LotusComponent getComponent() {
		return this.project.getComponent(0);
	}
	
}
