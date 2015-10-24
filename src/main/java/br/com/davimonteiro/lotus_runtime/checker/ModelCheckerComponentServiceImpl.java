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
import br.com.davimonteiro.lotus_runtime.ComponentManager;
import br.com.davimonteiro.lotus_runtime.checker.conditional.ConditionContext;
import br.com.davimonteiro.lotus_runtime.config.ConfigurationServiceComponent;
import br.com.davimonteiro.lotus_runtime.model.LotusModelServiceComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusComponent;
import br.com.davimonteiro.lotus_runtime.notifier.NotifierComponentService;
import br.com.davimonteiro.lotus_runtime.probabilisticReach.ProbabilisticReachAlgorithm;

@Slf4j
public class ModelCheckerComponentServiceImpl implements Component, ModelCheckerServiceComponent {
	
	private LotusModelServiceComponent modelComponent;
	
	private List<Property> properties;
	
	private ProbabilisticReachAlgorithm reachAlgorithm;
	
	private NotifierComponentService eventBusComponentService;
	
	private ConditionContext conditionContext;
	
	public ModelCheckerComponentServiceImpl() {
		this.reachAlgorithm = new ProbabilisticReachAlgorithm();
		this.conditionContext = new ConditionContext();
	}
	
	@Override
	public void start(ComponentManager manager) throws Exception {
		ConfigurationServiceComponent configurationComponent = manager.getComponentService(ConfigurationServiceComponent.class);
		this.properties = configurationComponent.getConfiguration().getProperties();
		this.modelComponent = manager.getComponentService(LotusModelServiceComponent.class);
		this.eventBusComponentService = manager.getComponentService(NotifierComponentService.class);
	}
	
	@Override
	public void verifyModel() {
		LotusComponent component = modelComponent.getLotusComponent();

		for (Property property : properties) {
			Double probabilityBetween = reachAlgorithm.probabilityBetween(component, property.getSourceStateId(), property.getTargetStateId());
			
			if (conditionContext.verify(property.getProbability(), property.getConditionalOperator(), probabilityBetween)) {
				eventBusComponentService.publish(property);
				log.info(property.toString());
			}
		}
	}
	
	@Override
	public void stop(ComponentManager manager) throws Exception {
		manager.uninstallComponent(this);
	}
	
}
