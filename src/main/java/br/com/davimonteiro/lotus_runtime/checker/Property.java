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
package br.com.davimonteiro.lotus_runtime.checker;

public class Property {

	private Integer id;
	
	private Integer sourceStateId;
	
	private Integer targetStateId;
	
	private ConditionalOperator conditionalOperator;
	
	private Double probability;
	
	public Property() { }
	
	private Property(PropertyBuilder builder) {
		this.id = builder.id;
		this.sourceStateId = builder.sourceStateId;
		this.targetStateId = builder.targetStateId;
		this.conditionalOperator = builder.conditionalOperator;
		this.probability = builder.probability;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSourceStateId() {
		return sourceStateId;
	}

	public void setSourceStateId(Integer sourceStateId) {
		this.sourceStateId = sourceStateId;
	}

	public Integer getTargetStateId() {
		return targetStateId;
	}

	public void setTargetStateId(Integer targetStateId) {
		this.targetStateId = targetStateId;
	}

	public ConditionalOperator getConditionalOperator() {
		return conditionalOperator;
	}

	public void setConditionalOperator(ConditionalOperator conditionalOperator) {
		this.conditionalOperator = conditionalOperator;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}
	
	public static class PropertyBuilder {
		
		private Integer id;
		
		private Integer sourceStateId;
		
		private Integer targetStateId;
		
		private ConditionalOperator conditionalOperator;
		
		private Double probability;
		
		public PropertyBuilder() { }
		
		public PropertyBuilder id(Integer id) {
			this.id = id;
			return this;
		}
		
		public PropertyBuilder sourceStateId(Integer sourceStateId) {
			this.sourceStateId = sourceStateId;
			return this;
		}
		
		public PropertyBuilder targetStateId(Integer targetStateId) {
			this.targetStateId = targetStateId;
			return this;
		}
		
		public PropertyBuilder conditionalOperator(ConditionalOperator conditionalOperator) {
			this.conditionalOperator = conditionalOperator;
			return this;
		}
		
		public PropertyBuilder probability(Double probability) {
			this.probability = probability;
			return this;
		}
		
		public Property build() {
			return new Property(this);
		}
		
	}
	
}