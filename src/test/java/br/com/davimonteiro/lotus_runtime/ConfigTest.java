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

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.davimonteiro.lotus_runtime.checker.ConditionalOperator;
import br.com.davimonteiro.lotus_runtime.checker.Property;
import br.com.davimonteiro.lotus_runtime.config.ConfigurationUtil;
import br.com.davimonteiro.lotus_runtime.config.LotusRuntimeConfiguration;

//https://marcinkubala.wordpress.com/2013/10/09/typesafe-config-hocon/

public class ConfigTest {

	public static void main(String[] args) {
		
		//Config config = ConfigFactory.load("application.json");
		
		// Properties that I want to verify
		List<Property> properties = new ArrayList<Property>();
		
		properties.add(Property.builder()
				.sourceStateId(0)
				.targetStateId(11)
				.conditionalOperator(ConditionalOperator.GREATER_THAN)
				.probability(0.9)
				.build());
		
		properties.add(Property.builder()
				.sourceStateId(0)
				.targetStateId(11)
				.conditionalOperator(ConditionalOperator.GREATER_THAN)
				.probability(0.9)
				.build());

		LotusRuntimeConfiguration config = LotusRuntimeConfiguration.builder()
				.projectFile("")
				.traceFile("")
				.milliseconds(1000L)
				.properties(properties)
				.build();
		
		
		
		// Save in a file
		Path path = Paths.get((System.getProperty(ConfigurationUtil.USER_HOME) + FileSystems.getDefault().getSeparator() +"Desktop"));
		ConfigurationUtil.save(path, config);
		
		System.out.println(ConfigurationUtil.load(path));

	}

}
