/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
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
package br.com.davimonteiro.lotus_runtime.config;

import java.util.List;

import br.com.davimonteiro.lotus_runtime.checker.Property;

public class Configuration {
	
	public static final String FILE_EXTENSION = ".json";
	
	private String name;
	
	private String traceFile;
	
	private String projectFile;
	
	private Long milliseconds;
	
	private List<Property> properties;
	
	public Configuration() { }

	private Configuration(ConfigurationBuilder builder) {
		this.name = builder.name;
		this.traceFile = builder.traceFile;
		this.projectFile = builder.projectFile;
		this.milliseconds = builder.milliseconds;
		this.properties = builder.properties;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTraceFile() {
		return traceFile;
	}

	public void setTraceFile(String traceFile) {
		this.traceFile = traceFile;
	}

	public String getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(String projectFile) {
		this.projectFile = projectFile;
	}

	public Long getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(Long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public static class ConfigurationBuilder {
		
		private String name;
		
		private String traceFile;
		
		private String projectFile;
		
		private Long milliseconds;
		
		private List<Property> properties;
		
		public ConfigurationBuilder() { }
		
		public ConfigurationBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public ConfigurationBuilder traceFile(String traceFile) {
			this.traceFile = traceFile;
			return this;
		}
		
		public ConfigurationBuilder projectFile(String projectFile) {
			this.projectFile = projectFile;
			return this;
		}
		
		public ConfigurationBuilder milliseconds(Long milliseconds) {
			this.milliseconds = milliseconds;
			return this;
		}
		
		public ConfigurationBuilder properties(List<Property> properties) {
			this.properties = properties;
			return this;
		}
		
		public Configuration build() {
			return new Configuration(this);
		}

		
	}
	
}
