package com.maveygravey;

import com.aparapi.Kernel;
import com.aparapi.Range;

import com.maveygravey.LinearAlgebra;

public class Simulation {

    public static void main(String[] args) {
        Vector input = LinearAlgebra.newVector(1, new float[] {1});
        NeuralNetwork nn = NeuralNetwork.newNeuralNetwork(new int[] {1, 2, 1});
        Vector output = nn.feedForward(input);
        System.out.println("Input Vector: " + input.getComponent(0));
        System.out.println("Output of Neural Network: " + output.getComponent(0));
        System.out.println("Network Weights Layer 1: " + nn.weights[0].getComponent(0, 0) + ' ' + nn.weights[0].getComponent(1, 0));
        System.out.println("Network Weights Layer 2: " + nn.weights[1].getComponent(0, 0) + ' ' + nn.weights[1].getComponent(0, 1));
        System.out.println("Network Biases Layer 1: " + nn.biases[0].getComponent(0) + ' ' + nn.biases[0].getComponent(1));
        System.out.println("Network Biases Layer 2: " + nn.biases[1].getComponent(0));
    }
}


//================================================================================================GPU ACCELERATION================================================================================================
// Kernel matrixMultiplyKernel = new Kernel() {
//             @Override
//             public void run() {
//                 int index = getGlobalId();  // Get global index
//                 int row = index / size;
//                 int col = index % size;
//                 float value = 0;

//                 // Perform matrix multiplication (A * B = C)
//                 for (int k = 0; k < size; k++) {
//                     value += matrixA[row * size + k] * matrixB[k * size + col];
//                 }
//                 matrixC_GPU[index] = value;
//             }
//         };

//         // Execute the kernel on the GPU
//         Range range = Range.create(size * size);
//         matrixMultiplyKernel.execute(range);
//================================================================================================Print Computation time================================================================================================
//        long startTimeGPU = System.nanoTime();
//        long endTimeGPU = System.nanoTime();
//        long durationGPU = endTimeGPU - startTimeGPU;
//       System.out.println("GPU Computation Time: " + durationGPU / 1_000_000 + " ms");