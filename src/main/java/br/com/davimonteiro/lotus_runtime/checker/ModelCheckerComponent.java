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
package br.com.davimonteiro.lotus_runtime.checker;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import br.com.davimonteiro.lotus_runtime.Component;
import br.com.davimonteiro.lotus_runtime.ComponentContext;
import br.com.davimonteiro.lotus_runtime.checker.conditional.ConditionContext;
import br.com.davimonteiro.lotus_runtime.eventbus.EventBusComponent;
import br.com.davimonteiro.lotus_runtime.model.LotusComponent;
import br.com.davimonteiro.lotus_runtime.probabilisticReach.ProbabilisticReachAlgorithm;
import br.com.davimonteiro.lotus_runtime.project.ProjectUtilComponent;

@Slf4j
public class ModelCheckerComponent implements Component {
	
	private ProjectUtilComponent projectUtilComponent;
	
	private List<Property> properties;
	
	private ProbabilisticReachAlgorithm reachAlgorithm;
	
	private EventBusComponent eventBusComponent;
	
	private ConditionContext conditionContext;
	
	public ModelCheckerComponent(List<Property> properties) {
		this.properties = properties;
		this.reachAlgorithm = new ProbabilisticReachAlgorithm();
		this.conditionContext = new ConditionContext();
	}
	
	@Override
	public void start(ComponentContext context) throws Exception {
		this.projectUtilComponent = context.getComponent(ProjectUtilComponent.class);
		this.eventBusComponent = context.getComponent(EventBusComponent.class);

//		Iterable<LotusTransition> transitions = projectUtilComponent.getComponent().getTransitions();
//		
//		for (LotusTransition transition : transitions) {
//			transition.addListener(listener);
//		}
	}
	
	
	public void verifyModel() {
		LotusComponent component = projectUtilComponent.getComponent();

		for (Property property : properties) {
			Double probabilityBetween = reachAlgorithm.probabilityBetween(component, property.getSourceStateId(), property.getTargetStateId());
			
			if (conditionContext.verify(property.getProbability(), property.getConditionalOperator(), probabilityBetween)) {
				eventBusComponent.publish(property);
				log.info(property.toString());
			}
		}
	}
	
	@Override
	public void stop(ComponentContext context) throws Exception { }
	
//	private LotusTransition.Listener listener = new LotusTransition.Listener() {
//		
//		@Override
//		public void onChange(LotusTransition transition) {
//			verifyModel();
//		}
//		
//	};

}
