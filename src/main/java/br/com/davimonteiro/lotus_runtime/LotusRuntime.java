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
package br.com.davimonteiro.lotus_runtime;

import java.nio.file.Paths;

import br.com.davimonteiro.lotus_runtime.checker.ModelCheckerComponentServiceImpl;
import br.com.davimonteiro.lotus_runtime.config.Configuration;
import br.com.davimonteiro.lotus_runtime.config.ConfigurationServiceComponentImpl;
import br.com.davimonteiro.lotus_runtime.model.ModelComponentImpl;
import br.com.davimonteiro.lotus_runtime.monitor.MonitorComponentServiceImpl;
import br.com.davimonteiro.lotus_runtime.notifier.NotifierComponentServiceImpl;
import br.com.davimonteiro.lotus_runtime.notifier.PropertyViolationHandler;

public class LotusRuntime {

	private Configuration configuration;
	
	private PropertyViolationHandler violationHandler;
	
	private ComponentManager manager;
	
	public LotusRuntime(Configuration configuration, PropertyViolationHandler violationHandler) {
		this.configuration = configuration;
		this.violationHandler = violationHandler;
		this.manager = new ComponentManagerImpl();
	}
	
	// TODO Para implementar. Cada componente deve ter uma prioridade para que ele seja inciado.
	public void start() throws Exception {
		
		// Configuration
		ConfigurationServiceComponentImpl configurationServiceComponent = new ConfigurationServiceComponentImpl();
		configurationServiceComponent.setConfiguration(configuration);
		
		
		manager.installComponent(configurationServiceComponent);
		
		manager.installComponent(new NotifierComponentServiceImpl(violationHandler));
		manager.installComponent(new ModelComponentImpl(Paths.get(configuration.getProjectFile())));
		manager.installComponent(new MonitorComponentServiceImpl(Paths.get(configuration.getTraceFile()), configuration.getMilliseconds()));
		manager.installComponent(new ModelCheckerComponentServiceImpl(configuration.getProperties()));
		
		manager.start(manager);
	}

	public void stop() throws Exception {
		manager.stop(manager);
	}

}
