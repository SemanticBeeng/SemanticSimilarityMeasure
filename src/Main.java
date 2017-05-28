/**
 * Created by Keetmalin on 5/25/2017
 * Project - SemanticSimilarityMeasure
 */

import java.io.*;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        //reading from CSV file
        CSV csvObject = new CSV();

        ValueMatrix[] valueMatrices = new ValueMatrix[100];

        //call word2vec model
        Word2vecModel word2vecModel = new Word2vecModel();

        try {
            File file = new File("src/valueMatrices.ser");
            if (file.exists()) {

                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                valueMatrices = (ValueMatrix[]) in.readObject();
                in.close();
                fileIn.close();
            } else {

                valueMatrices = new ValueMatrix[100];
                int i = 0;
                for (String term : csvObject.getTermList()) {

                    Matrix matrixObject = new Matrix(term, word2vecModel, csvObject);
                    Normalize normalize = new Normalize(matrixObject.getMatrix());
                    ValueMatrix valueMatrix = new ValueMatrix(term, csvObject, word2vecModel, normalize);
                    Rmatrix rmatrix = new Rmatrix(term, csvObject, word2vecModel);
                    valueMatrix.setRmatrix(rmatrix);

                    valueMatrices[i] = valueMatrix;
                    System.out.println("Value Matrix for term = " + term + " , done");

                    i++;


                    //delete this. This is to avoid the for loop from calculating all 100 terms. This will skip after the first term "layer"
                    //if (Objects.equals(term, "lawyer")) break;

                }

            }




            //Serialize the ValueMatrix Array to src
            FileOutputStream fileOut =
                    new FileOutputStream("src/valueMatrices.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(valueMatrices);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in src/valueMatrices.ser");
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }


        //initiate Neural Network
        NeuralNetwork neuralNetwork = new NeuralNetwork(Constants.NN_INPUT_LAYER, Constants.NN_HIDDEN_LAYER, Constants.NN_OUTPUT_LAYER);
        int maxRuns = 50000;
        double minErrorCondition = 0.001;
        neuralNetwork.setInputVector(valueMatrices);
        neuralNetwork.run(maxRuns, minErrorCondition, csvObject);


    }




}

