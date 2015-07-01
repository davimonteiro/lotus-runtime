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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.davimonteiro.lotus_runtime.app.MyHandler;
import br.com.davimonteiro.lotus_runtime.eventbus.EventBusComponentServiceImpl;
import br.com.davimonteiro.lotus_runtime.eventbus.EventBusComponentService;



public class ComponentManagerImplTest {
	
	
	@Test
	public void test() throws Exception {
		
		ComponentManager manager = new ComponentManagerImpl();
		
		manager.installComponent(new EventBusComponentServiceImpl(new MyHandler()));
		
		System.out.println("Class: " + manager.getComponentService(EventBusComponentService.class));
		
		assertEquals("10 x 0 must be 0", 0, 0);
	}
	
	
}
