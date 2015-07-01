package br.com.davimonteiro.lotus_runtime.eventbus;

import br.com.davimonteiro.lotus_runtime.ComponentService;
import br.com.davimonteiro.lotus_runtime.checker.Property;

public interface EventBusComponentService extends ComponentService {
	
	public void publish(Property condition);

}
