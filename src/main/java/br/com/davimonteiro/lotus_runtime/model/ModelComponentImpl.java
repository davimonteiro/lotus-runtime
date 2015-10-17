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
package br.com.davimonteiro.lotus_runtime.model;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.config.ConfigurationServiceComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusModel;

@Slf4j
public class ModelComponentImpl implements Component, ModelServiceComponent {

	private Path projectFile;
	
	private LotusModel lotusModel;
	
	private ProjectSerializer serializer;
	
	
	public ModelComponentImpl() {
		this.serializer = new ProjectXMLSerializer();
	}

	@Override
	public void start(ComponentManager manager) throws Exception {
		ConfigurationServiceComponent configurationComponent = manager.getComponentService(ConfigurationServiceComponent.class);
		this.projectFile = Paths.get(configurationComponent.getConfiguration().getProjectFile());
		loadProject();
	}
	
	@Override
	public void stop(ComponentManager manager) throws Exception {
		saveProject();
		manager.uninstallComponent(this);
	}

	private void saveProject() throws IOException, Exception {
		OutputStream outputStream = Files.newOutputStream(projectFile);
		serializer.toStream(lotusModel, outputStream);
	}
	
	private void loadProject() {
		try (InputStream in = Files.newInputStream(projectFile)) {
			lotusModel = serializer.parseStream(in);
			lotusModel.setValue("file", projectFile.toFile());
			
			if (isNullOrEmpty(lotusModel.getName())) {
				lotusModel.setName("Untitled");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}
	
	@Override
	@Synchronized
	public void updateLotusModel(LotusModel project) {
		this.lotusModel = project;

		try {
			saveProject();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@Override
	@Synchronized
	public LotusModel getLotusModel() {
		return this.lotusModel;
	}
	
	@Override
	@Synchronized
	public LotusComponent getLotusComponent() {
		return this.lotusModel.getComponent(0);
	}
	
}
