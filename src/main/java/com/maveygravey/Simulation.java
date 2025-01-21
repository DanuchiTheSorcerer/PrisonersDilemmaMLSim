package com.maveygravey;

import com.aparapi.Kernel;
import com.aparapi.Range;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Simulation {
    Player[] players = new Player[100];
    public static void main(String[] args) {
        Simulation sim = new Simulation();
        for (int i = 0; i < 100; i++) {
            sim.players[i] = Player.newPlayer(null);
        }
        for (int i =0;i<10000;i++) {
            sim.runSimulation();
        }
    }
    public void runSimulation() {
        final int numPlayers = 100;
        final Simulation sim = this;
    
        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int globalId = getGlobalId();
                int i = globalId / numPlayers;
                int j = globalId % numPlayers;
                if (i < j) {
                    sim.matchPlayers(i, j);
                }
            }
        };
    
        int totalMatches = 10000;
        Range range = Range.create(totalMatches);
        kernel.execute(range);
        
        // Sort players by score in descending order
        Arrays.sort(players, Comparator.comparingInt((Player p) -> p.score).reversed());

        // Print the scores and percentage of defections for the top 50 players
        for (int i = 0; i < 50; i++) {
            Player player = players[i];
            float percentageDefected = (player.defections*100 / (player.cooperations+player.defections));
            System.out.println("Player " + i + ": Score = " + player.score + ", Percentage Defected = " + percentageDefected + "%");
        }

        // Reproduce the top 50 players twice and mutate
        for (int i = 0; i < 50; i++) {
            Player parent = players[i];
            Player offspring = Player.newPlayer(parent.brain);
            offspring.mutate();
            players[50 + i] = offspring;
        }
        for (int i = 0; i < 50; i++) {
            players[i].score = 0;
            players[i].defections = 0;
            players[i].cooperations = 0;
        }
    }
    public void matchPlayers(int p1Index,int p2Index) {
        Player p1 = players[p1Index];
        Player p2 = players[p2Index];
        int[] p1Moves = new int[50];
        int[] p2Moves = new int[50];
        p1Moves[0] = 1;
        p2Moves[0] = 1;
        p1Moves[1] = 1;
        p2Moves[1] = 1;
        for (int i = 2;i<50;i++) {
            p1Moves[i] = p1.makeDecision(p1Moves[i-2],p1Moves[i-1],p2Moves[i-2],p2Moves[i-1],(float) i / 50);
            p2Moves[i] = p2.makeDecision(p1Moves[i-2],p1Moves[i-1],p2Moves[i-2],p2Moves[i-1],(float) i / 50);
            if (p1Moves[i] == 0 && p2Moves[i] == 0) {
                p1.score += 1;
                p2.score += 1;
                p1.defections += 1;
                p2.defections += 1;
            } else if (p1Moves[i] == 0 && p2Moves[i] == 1) {
                p1.score += 5;
                p2.score += 0;
                p1.defections += 1;
                p2.cooperations += 1;
            } else if (p1Moves[i] == 1 && p2Moves[i] == 0) {
                p1.score += 0;
                p2.score += 5;
                p1.cooperations += 1;
                p2.defections += 1;
            } else {
                p1.score += 3;
                p2.score += 3;
                p1.cooperations += 1;
                p2.cooperations += 1;
            }
        }
    }
}

class Player {
    NeuralNetwork brain;
    int score = 0;
    int cooperations = 0;
    int defections = 0;
    public static Player newPlayer(NeuralNetwork nn) {
        Player player = new Player();
        if (nn == null) {
            player.brain = NeuralNetwork.newNeuralNetwork(new int[] {6, 6, 2});
        } else {
            player.brain = nn;
        }
        return player;
    }
    public int makeDecision(int m1,int m2,int m3,int m4,float r) {
        Vector input = LinearAlgebra.newVector(6, new float[] {m1,m2,m3,m4,r,1});
        Vector output = brain.feedForward(input);
        Vector decision = output.softmax();
        Random rand = new Random();
        if (decision.getComponent(0) > rand.nextFloat()) {
            return 0;
        } else {
            return 1;
        }
    }
    public void mutate() {
        Random rand = new Random();
        for (int n = 0; n < 10; n++) {
            // Randomly choose to mutate a weight or a bias
            if (rand.nextBoolean()) {
                // Mutate a weight
                int layer = rand.nextInt(brain.weights.length);
                int row = rand.nextInt(brain.weights[layer].getRows());
                int col = rand.nextInt(brain.weights[layer].getCols());
                float mutation = (rand.nextBoolean() ? 0.02f : -0.02f);
                brain.weights[layer].set(row, col, brain.weights[layer].getComponent(row, col) + mutation);
            } else {
                // Mutate a bias
                int layer = rand.nextInt(brain.biases.length);
                int index = rand.nextInt(brain.biases[layer].getSize());
                float mutation = (rand.nextBoolean() ? 0.01f : -0.01f);
                brain.biases[layer].set(index, brain.biases[layer].getComponent(index) + mutation);
            }
        }
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