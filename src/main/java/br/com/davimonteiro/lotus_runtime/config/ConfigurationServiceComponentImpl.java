package br.com.davimonteiro.lotus_runtime.config;

import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.notifier.ViolationHandler;

public class ConfigurationServiceComponentImpl implements ConfigurationServiceComponent, Component {

	private Configuration configuration;
	
	private ViolationHandler violationHandler;

	@Override
	public void start(ComponentManager manager) throws Exception {
		
	}

	@Override
	public void stop(ComponentManager manager) throws Exception {
		manager.uninstallComponent(this);
	}
	
	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public ViolationHandler getViolationHandler() {
		return violationHandler;
	}

	@Override
	public void setViolationHandler(ViolationHandler violationHandler) {
		this.violationHandler = violationHandler;
	}

}
