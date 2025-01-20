package com.maveygravey;

import com.aparapi.Kernel;
import com.aparapi.Range;

import com.maveygravey.LinearAlgebra;

public class Simulation {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LinearAlgebra.main(new String[] { "run" });
        System.out.println(" " + LinearAlgebra.newVector(0, new float[] {1, 2, 3}).getComponent(2));
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