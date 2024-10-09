package com.cybernetic;

import java.util.ArrayList;
import java.util.List;

public class OrganCompatibilityAnalyzer {
    private List<Organ> organs;
    private List<Patient> patients;

    public OrganCompatibilityAnalyzer() {
        organs = new ArrayList<>();
        patients = new ArrayList<>();
    }

    public void addOrgan(Organ organ) {
        organs.add(organ);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public int[][] createCompatibilityMatrix() {
        int[][] matrix = new int[organs.size()][patients.size() * 3]; // 3 factors: blood type, weight, HLA

        for (int i = 0; i < organs.size(); i++) {
            Organ organ = organs.get(i);
            for (int j = 0; j < patients.size(); j++) {
                Patient patient = patients.get(j);

                matrix[i][j * 3] = calculateBloodTypeCompatibility(organ.getBloodType(), patient.getBloodType());
                matrix[i][j * 3 + 1] = calculateWeightCompatibility(organ.getWeight(), patient.getWeight());
                matrix[i][j * 3 + 2] = calculateHlaCompatibility(organ.getHlaType(), patient.getHlaType());
            }
        }
        return matrix;
    }

    private int calculateBloodTypeCompatibility(String donorType, String recipientType) {
        if (donorType.equals(recipientType)) {
            return 100;
        } else if (donorType.equals("O")) {
            return 80;
        } else {
            return 0;
        }
    }

    private int calculateWeightCompatibility(int organWeight, int patientWeight) {
        double ratio = (double) organWeight / (patientWeight * 1000.0);
        if (ratio >= 0.8 && ratio <= 1.2) {
            return 100;
        } else if (ratio >= 0.6 && ratio <= 1.4) {
            return 50;
        } else {
            return 0;
        }
    }

    // Updated calculateHlaCompatibility method
    private int calculateHlaCompatibility(String donorHla, String recipientHla) {
        String[] donorHlaTypes = donorHla.split("-");
        String[] recipientHlaTypesArray = recipientHla.split("-");

        int matchCount = 0;
        for (String hla : donorHlaTypes) {
            for (String recipientHlaType : recipientHlaTypesArray) {
                if (hla.equals(recipientHlaType)) {
                    matchCount++;
                }
            }
        }
        double matchPercentage = (double) matchCount / donorHlaTypes.length * 100;
        return (int) matchPercentage;
    }

    public double[][] calculateWeightedCompatibility(double[] weights) {
        int[][] compatibilityMatrix = createCompatibilityMatrix();
        double[][] resultMatrix = new double[organs.size()][patients.size()];

        for (int i = 0; i < organs.size(); i++) {
            for (int j = 0; j < patients.size(); j++) {
                double bloodTypeScore = compatibilityMatrix[i][j * 3] * weights[0];
                double weightScore = compatibilityMatrix[i][j * 3 + 1] * weights[1];
                double hlaScore = compatibilityMatrix[i][j * 3 + 2] * weights[2];

                resultMatrix[i][j] = bloodTypeScore + weightScore + hlaScore;
            }
        }
        return resultMatrix;
    }

    public void displayMatrix(int[][] matrix) {
        System.out.println("Initial Compatibility Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("O" + (i + 1) + " ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void displayWeightMatrix(double[] weights) {
        System.out.println("\nWeight Matrix:");
        for (double weight : weights) {
            System.out.printf("%.2f  ", weight);
        }
        System.out.println();
    }

    public void displayWeightedMatrix(double[][] matrix) {
        System.out.println("\nFinal Weighted Compatibility Matrix:");
        System.out.print("     ");
        for (int j = 0; j < patients.size(); j++) {
            System.out.print("P" + (j + 1) + "   ");
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.print("O" + (i + 1) + " ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.1f ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}

