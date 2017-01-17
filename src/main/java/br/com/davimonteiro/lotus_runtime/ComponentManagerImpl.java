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
package br.com.davimonteiro.lotus_runtime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ComponentManagerImpl implements ComponentManager {
	
	private static final Logger log = Logger.getLogger(ComponentManagerImpl.class.getName());
	
	private List<Component> components;
	
	public ComponentManagerImpl() {
		this.components = new CopyOnWriteArrayList<Component>();
	}
		
	@Override
	public void start(ComponentManager manager) throws Exception {
		for (Component component : components) {
			log.info("Starting the component: " + component.getClass());
			component.start(this);
		}
	}

	@Override
	public void stop(ComponentManager manager) throws Exception {
		for (Component component : components) {
			log.info("Stopping the component: " + component.getClass());
			component.stop(this);
		}
	}
	
	@Override
	public <T extends ComponentService> T getComponentService(Class<T> clazz) {
		T service = null;
		
		for (Component component : components) {
			if (clazz.isAssignableFrom(component.getClass())) {
				service = clazz.cast(component);
				break;
			}
		}
		
		return service;
	}
	
	@Override
	public void installComponent(Component component) throws Exception {
		log.info("Installing the component: " + component.getClass());
		components.add(component);
	}

	@Override
	public void uninstallComponent(Component component) throws Exception {
		log.info("Uninstalling the component: " + component.getClass());
		components.remove(component);
	}

}
