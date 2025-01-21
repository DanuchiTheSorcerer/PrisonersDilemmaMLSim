package com.maveygravey;

import java.util.Random;

public class NeuralNetwork {
    int[] layerSizes;
    Matrix[] weights;
    Vector[] biases;
    public static void main(String[] args) {
        
    }
    public Vector feedForward(Vector input) {
        Vector a = input;
        for (int i = 0; i < layerSizes.length - 1; i++) {
            Vector z = weights[i].transform(a).add(biases[i]);
            a = LinearAlgebra.activationFunction(z);
        }
        return a;
    }
    public static NeuralNetwork newNeuralNetwork(int[] layerSizes) {
        NeuralNetwork nn = new NeuralNetwork();
        nn.layerSizes = layerSizes;
        nn.weights = new Matrix[layerSizes.length - 1];
        nn.biases = new Vector[layerSizes.length - 1];
        Random rand = new Random();
        
        for (int i = 0; i < layerSizes.length - 1; i++) {
            int fan_in = layerSizes[i];
            float stddev = (float) Math.sqrt(2.0 / fan_in);
            float[][] weightData = new float[layerSizes[i + 1]][layerSizes[i]];
            
            for (int j = 0; j < layerSizes[i + 1]; j++) {
                for (int k = 0; k < layerSizes[i]; k++) {
                    weightData[j][k] = (float) rand.nextGaussian() * stddev;
                }
            }
            
            nn.weights[i] = Matrix.newMatrix(layerSizes[i + 1], layerSizes[i], weightData);
            nn.biases[i] = Vector.newVector(layerSizes[i + 1], new float[layerSizes[i + 1]]); // Biases initialized to zero
        }
        return nn;
    }
}
