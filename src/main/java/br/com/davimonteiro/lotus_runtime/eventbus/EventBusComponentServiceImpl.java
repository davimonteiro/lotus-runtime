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
package br.com.davimonteiro.lotus_runtime.eventbus;

import net.engio.mbassy.bus.MBassador;
import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.checker.Property;

public class EventBusComponentServiceImpl implements Component, EventBusComponentService {

	private MBassador<Property> eventBus;

	private PropertyViolationHandler violationHandler;
	
	public EventBusComponentServiceImpl(PropertyViolationHandler violationHandler) {
		this.eventBus = new MBassador<Property>();
		this.violationHandler = violationHandler;
	}
	
	@Override
	public void start(ComponentManager manager) throws Exception {
		eventBus.subscribe(violationHandler);
	}

	@Override
	public void stop(ComponentManager manager) throws Exception {
		eventBus.unsubscribe(violationHandler);
		eventBus.shutdown();
		manager.uninstallComponent(this);
	}
	
	@Override
	public void publish(Property condition) {
		eventBus.publishAsync(condition);
	}
	
}
