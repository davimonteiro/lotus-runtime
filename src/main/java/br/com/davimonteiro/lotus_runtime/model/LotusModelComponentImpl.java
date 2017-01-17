/**
 * The MIT License
 * Copyright © 2016 Davi Monteiro Barbosa
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

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.google.common.base.Throwables;

import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.config.ConfigurationServiceComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusModel;
import br.com.davimonteiro.lotus_runtime.monitor.ProbabilisticAnnotator;
import net.engio.mbassy.listener.Synchronized;

public class LotusModelComponentImpl implements Component, LotusModelServiceComponent {
	
	private static final Logger log = Logger.getLogger(LotusModelComponentImpl.class.getName());
	
	private Path projectFile;
	
	private LotusModel lotusModel;
	
	private ProbabilisticAnnotator annotator;
	
	private ProjectSerializer serializer;
	
	
	public LotusModelComponentImpl() {
		this.serializer = new ProjectXMLSerializer();
		this.annotator = new ProbabilisticAnnotator();
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

	private void saveProject() {
		try (OutputStream outputStream = Files.newOutputStream(projectFile)){
			serializer.toStream(lotusModel, outputStream);
		} catch (Exception e) {
			log.severe(e.getMessage());
			Throwables.propagate(e);
		}
	}
	
	private void loadProject() {
		try (InputStream in = Files.newInputStream(projectFile)) {
			lotusModel = serializer.parseStream(in);
			lotusModel.setValue("file", projectFile.toFile());
			
			if (isNullOrEmpty(lotusModel.getName())) {
				lotusModel.setName("Untitled");
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			Throwables.propagate(e);
		}
	}
	
	@Override
	@Synchronized
	public void updateLotusModel(String[] trace) {
		this.annotator.annotate(getLotusComponent(), trace);
		saveProject();
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
