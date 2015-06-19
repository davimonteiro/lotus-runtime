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

import br.com.davimonteiro.lotus_runtime.model.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.LotusState;
import br.com.davimonteiro.lotus_runtime.model.LotusTransition;

/**
 *
 * @author Ranniery
 */
public class ProbabilisticReachAlgorithm {
    
    public double probabilityBetween (LotusComponent a, Integer sourceId, Integer targetId) {
        int tam = a.getStatesCount();
        double[][] probabilities = new double[tam][tam];
        probabilities = zerar(probabilities, tam);
        List<LotusTransition> transitions = transitionsList(a);
        
        
        for(LotusTransition t : transitions){
            sourceId = t.getSource().getID();
            targetId = t.getDestiny().getID();
            probabilities[sourceId][targetId] = t.getProbability();
        }
        zerarDiagonal(probabilities, tam);
        
//      multiplica as matrizes um monte de vez
        double[][] mult = probabilities;
//      visualization
//        for (int p = 0 ; p < tam ; p++ ){
//            for (int q = 0 ; q < tam ; q++ ){
//               System.out.print(mult[p][q]+"\t");
//            }
//            System.out.print("\n");
//        }
//        System.out.println("------------------------------------------------");
        double difference = 1;
        double total = 0;
        int count = 0;
        while(abs(difference) > 0.01 || count < 10){
            total += probabilities[sourceId][targetId];
            difference = probabilities[sourceId][targetId];
            probabilities = multiply(probabilities, mult,tam);
            difference = difference - probabilities[sourceId][targetId];
            count++;
            if(abs(difference) > 0.0001){
                count = 0;
            }
        }
        if(total > 1){
           total = 1;
        }
//      visualization        
//        for (int p = 0 ; p < tam ; p++ ){
//            for (int q = 0 ; q < tam ; q++ ){
//               System.out.print(probabilities[p][q]+"\t");
//            }
//            System.out.print("\n");
//        }
//        i = source.getID();
//        j = destination.getID();
        total = ProbabilisticReachAlgorithm.truncar(total, 5);
//        JOptionPane.showMessageDialog(null, "Probability to reach state " + destination + " from state " + source + " is: " + total);
        return total;
    }
    
    public double[][] multiply(double[][] matrixA, double[][] matrixB, int tam){
        double sum = 0;
        double[][] multiply = new double[tam][tam];
        for (int i = 0 ; i < tam ; i++ )
         {
            for (int j = 0 ; j < tam ; j++ )
            {   
               for (int k = 0 ; k < tam ; k++ )
               {
                  sum += matrixA[i][k]*matrixB[k][j];
               }
 
               multiply[i][j] = sum;
               sum = 0;
            }
        }
        return multiply;
    }
    
    public List<LotusTransition> transitionsList(LotusComponent a){
        Iterable<LotusTransition> aux = a.getTransitions();
        List<LotusTransition> aux2 = new ArrayList();
        for(LotusTransition t : aux){
            aux2.add(t);
        }
        return aux2;
    }
    
    public int getStatesSize(Iterable<LotusState> states){
    	
    	// TODO Refactor this
    	Iterables.size(states);
    	
        List<LotusState> statesL = new ArrayList();
        for(LotusState aux : states){
            statesL.add(aux);
        }
        int size = statesL.size();
        return size;
    }
    
    public double[][] zerar (double[][] probabilities, int tam){
        for(int i = 0; i < tam; i++){
            for(int j = 0; j < tam; j++){
                probabilities[i][j] = 0;
            }
        }
        return probabilities;
    }
    
    public double[][] zerarDiagonal (double[][] probabilities, int tam){
        for(int i = 0; i < tam; i++){
            if(probabilities[i][i] == 1){
                probabilities[i][i] = 0;
            }
        }
        return probabilities;
    }
    
    public static double truncar(double d, int casas_decimais){  
  
        int var1 = (int) d;   // Remove a parte decimal do número... 2.3777 fica 2  
        double var2 = var1*Math.pow(10,casas_decimais); // adiciona zeros..2.0 fica 200.0  
        double var3 = (d - var1)*Math.pow(10,casas_decimais); /** Primeiro retira a parte decimal fazendo 2.3777 - 2 ..fica 0.3777, depois multiplica por 10^(casas decimais) 
        por exemplo se o número de casas decimais que queres considerar for 2, então fica 0.3777*10^2 = 37.77 **/  
        int var4 = (int) var3; // Remove a parte decimal da var3, ficando 37  
        int var5 = (int) var2; // Só para não haver erros de precisão: 200.0 passa a 200  
        int resultado = var5+var4; // O resultado será 200+37 = 237  
        double resultado_final = resultado/Math.pow(10,casas_decimais); // Finalmente divide-se o resultado pelo número de casas decimais, 237/100 = 2.37  
        return resultado_final; // Retorna o resultado_final :P   
    } 
}
