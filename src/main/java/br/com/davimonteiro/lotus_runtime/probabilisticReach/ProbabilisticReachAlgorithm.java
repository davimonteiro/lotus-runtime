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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davimonteiro.lotus_runtime.probabilisticReach;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;

import br.com.davimonteiro.lotus_runtime.model.util.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusState;
import br.com.davimonteiro.lotus_runtime.model.util.LotusTransition;

/**
 *
 * @author Ranniery
 */
public class ProbabilisticReachAlgorithm {

	public double probabilityBetween(LotusComponent a, Integer sourceId, Integer targetId) {
		int tam = a.getStatesCount();
		double[][] probabilities = new double[tam][tam];
		probabilities = zerar(probabilities, tam);
		List<LotusTransition> transitions = transitionsList(a);

		for (LotusTransition t : transitions) {
			sourceId = t.getSource().getID();
			targetId = t.getDestiny().getID();
			probabilities[sourceId][targetId] = t.getProbability();
		}
		zerarDiagonal(probabilities, tam);

		double[][] mult = probabilities;
		double difference = 1;
		double total = 0;
		int count = 0;
		while (abs(difference) > 0.01 || count < 10) {
			total += probabilities[sourceId][targetId];
			difference = probabilities[sourceId][targetId];
			probabilities = multiply(probabilities, mult, tam);
			difference = difference - probabilities[sourceId][targetId];
			count++;
			if (abs(difference) > 0.0001) {
				count = 0;
			}
		}
		if (total > 1) {
			total = 1;
		}
		total = truncar(total, 5);
		return total;
	}

	public double[][] multiply(double[][] matrixA, double[][] matrixB, int tam) {
		double sum = 0;
		double[][] multiply = new double[tam][tam];
		for (int i = 0; i < tam; i++) {
			for (int j = 0; j < tam; j++) {
				for (int k = 0; k < tam; k++) {
					sum += matrixA[i][k] * matrixB[k][j];
				}

				multiply[i][j] = sum;
				sum = 0;
			}
		}
		return multiply;
	}

	public List<LotusTransition> transitionsList(LotusComponent a) {
		Iterable<LotusTransition> aux = a.getTransitions();
		List<LotusTransition> aux2 = new ArrayList<LotusTransition>();
		for (LotusTransition t : aux) {
			aux2.add(t);
		}
		return aux2;
	}

	public int getStatesSize(Iterable<LotusState> states) {
		return Iterables.size(states);
	}

	public double[][] zerar(double[][] probabilities, int tam) {
		for (int i = 0; i < tam; i++) {
			for (int j = 0; j < tam; j++) {
				probabilities[i][j] = 0;
			}
		}
		return probabilities;
	}

	public double[][] zerarDiagonal(double[][] probabilities, int tam) {
		for (int i = 0; i < tam; i++) {
			if (probabilities[i][i] == 1) {
				probabilities[i][i] = 0;
			}
		}
		return probabilities;
	}

	public double truncar(double d, int casas_decimais) {
		int var1 = (int) d;
		double var2 = var1 * Math.pow(10, casas_decimais);
		double var3 = (d - var1) * Math.pow(10, casas_decimais);
		int var4 = (int) var3;
		int var5 = (int) var2;
		int resultado = var5 + var4;
		double resultado_final = resultado / Math.pow(10, casas_decimais);
		return resultado_final;
	}

}
