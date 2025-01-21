package com.maveygravey;

public class LinearAlgebra {
    public static void main(String[] args) {
        Vector testVector = Vector.newVector(3, new float[] {1, 2, 3});
        Matrix testMatrix = Matrix.newMatrix(3, 3, new float[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
    }
    public static Vector newVector(int size, float[] components) {
        return Vector.newVector(size, components);
    }
    public static Matrix newMatrix(int rows, int cols, float[][] components) {
        return Matrix.newMatrix(rows, cols, components);
    }
    public static Vector activationFunction(Vector v) {
        float[] newComponents = new float[v.getSize()];
        for (int i = 0; i < v.getSize(); i++) {
            newComponents[i] = Math.max(0, v.getComponent(i));
        }
        return Vector.newVector(v.getSize(), newComponents);
    }
}

class Vector {
    private float[] components;
    public static Vector newVector(int size, float[] components) {
        Vector v = new Vector();
        v.components = components;
        return v;
    }
    public float getComponent(int index) {
        return components[index];
    }
    public int getSize() {
        return components.length;
    }
    public Vector add(Vector other) {
        float[] newComponents = new float[components.length];
        for (int i = 0; i < components.length; i++) {
            newComponents[i] = components[i] + other.components[i];
        }
        return Vector.newVector(components.length, newComponents);
    }
    public Vector subtract(Vector other) {
        float[] newComponents = new float[components.length];
        for (int i = 0; i < components.length; i++) {
            newComponents[i] = components[i] - other.components[i];
        }
        return Vector.newVector(components.length, newComponents);
    }
    public Vector scale(float scalar) {
        float[] newComponents = new float[components.length];
        for (int i = 0; i < components.length; i++) {
            newComponents[i] = components[i] * scalar;
        }
        return Vector.newVector(components.length, newComponents);
    }
    public float dot(Vector other) {
        float result = 0;
        for (int i = 0; i < components.length; i++) {
            result += components[i] * other.components[i];
        }
        return result;
    }
    public Vector softmax() {
        float sum = 0;
        for (int i = 0; i < components.length; i++) {
            sum += Math.exp(components[i]);
        }
        float[] newComponents = new float[components.length];
        for (int i = 0; i < components.length; i++) {
            newComponents[i] = (float) Math.exp(components[i]) / sum;
        }
        return Vector.newVector(components.length, newComponents);
    }
    public void set(int index, float value) {
        components[index] = value;
    }
}

class Matrix {
    private float[][] components;
    public static Matrix newMatrix(int rows, int cols, float[][] components) {
        Matrix m = new Matrix();
        m.components = components;
        return m;
    }
    public int getRows() {
        return components.length;
    }
    public int getCols() {
        return components[0].length;
    }
    public float getComponent(int row, int col) {
        return components[row][col];
    }
    public Matrix add(Matrix other) {
        float[][] newComponents = new float[components.length][components[0].length];
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[0].length; j++) {
                newComponents[i][j] = components[i][j] + other.components[i][j];
            }
        }
        return Matrix.newMatrix(components.length, components[0].length, newComponents);
    }
    public Matrix subtract(Matrix other) {
        float[][] newComponents = new float[components.length][components[0].length];
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[0].length; j++) {
                newComponents[i][j] = components[i][j] - other.components[i][j];
            }
        }
        return Matrix.newMatrix(components.length, components[0].length, newComponents);
    }
    public Matrix scale(float scalar) {
        float[][] newComponents = new float[components.length][components[0].length];
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[0].length; j++) {
                newComponents[i][j] = components[i][j] * scalar;
            }
        }
        return Matrix.newMatrix(components.length, components[0].length, newComponents);
    }
    public Matrix multiply(Matrix other) {
        float[][] newComponents = new float[components.length][other.components[0].length];
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < other.components[0].length; j++) {
                float sum = 0;
                for (int k = 0; k < components[0].length; k++) {
                    sum += components[i][k] * other.components[k][j];
                }
                newComponents[i][j] = sum;
            }
        }
        return Matrix.newMatrix(components.length, other.components[0].length, newComponents);
    }
    public Vector transform(Vector v) {
        float[] newComponents = new float[components.length];
        for (int i = 0; i < components.length; i++) {
            float sum = 0;
            for (int j = 0; j < components[0].length; j++) {
                sum += components[i][j] * v.getComponent(j);
            }
            newComponents[i] = sum;
        }
        return Vector.newVector(components.length, newComponents);
    }
    public void set(int row, int col, float value) {
        components[row][col] = value;
    }
}