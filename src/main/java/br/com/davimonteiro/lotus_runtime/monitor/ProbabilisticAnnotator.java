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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davimonteiro.lotus_runtime.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.davimonteiro.lotus_runtime.model.util.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusState;
import br.com.davimonteiro.lotus_runtime.model.util.LotusTransition;

/**
 *
 * @author emerson
 */
public class ProbabilisticAnnotator {
	
    private LotusState mCurrentState;
    private static final String VISIT_COUNT = "visit.count";

    public void annotate(LotusComponent c, InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] trace = line.split(",");
                mCurrentState = c.getInitialState();
                for (String event: trace) {                    
                    step(event.trim());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProbabilisticAnnotator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param component
     * @param trace
     */
	public void annotate(LotusComponent component, String[] trace) {
		mCurrentState = component.getInitialState();
		for (String event : trace) {
			step(event.trim());
		}
	}

    private void step(String event) {        
        LotusTransition ct = mCurrentState.getTransitionByLabel(event);
        if (ct == null) {
            System.out.println("invalid event " + event + " at state " + mCurrentState.getLabel());
            return;
        }
        int updatedStateVisitCount = getStateVisitCount(mCurrentState) + 1;
        setStateVisitCount(mCurrentState, updatedStateVisitCount);
        for (LotusTransition t: mCurrentState.getOutgoingTransitions()) {                        
            int transitionCount = getTransitionCount(t);    
            if (t == ct) {
                transitionCount += 1;
                setTransitionCount(t, transitionCount);
            }
            t.setProbability(((double) transitionCount) / updatedStateVisitCount);
        }
        mCurrentState = ct.getDestiny();
        
    }

    private int getStateVisitCount(LotusState s) {
        Object obj = s.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setStateVisitCount(LotusState s, int value) {
        s.setValue(VISIT_COUNT, value);
    }

    private int getTransitionCount(LotusTransition t) {
        Object obj = t.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setTransitionCount(LotusTransition t, int v) {
        t.setValue(VISIT_COUNT, v);
    }

}
