package br.com.davimonteiro.lotus_runtime.config;

import br.com.davimonteiro.lotus_runtime.ComponentService;

public interface ConfigurationServiceComponent extends ComponentService {
	
	Configuration getConfiguration();
	
	void setConfiguration(Configuration configuration);
	
}
