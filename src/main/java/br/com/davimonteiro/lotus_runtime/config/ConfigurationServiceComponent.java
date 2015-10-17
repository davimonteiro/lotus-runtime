package br.com.davimonteiro.lotus_runtime.config;

import br.com.davimonteiro.lotus_runtime.ComponentService;
import br.com.davimonteiro.lotus_runtime.notifier.ViolationHandler;

public interface ConfigurationServiceComponent extends ComponentService {
	
	Configuration getConfiguration();
	
	void setConfiguration(Configuration configuration);
	
	ViolationHandler getViolationHandler();
	
	void setViolationHandler(ViolationHandler violationHandler);
	
}
