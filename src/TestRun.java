import java.io.*;
import java.util.Arrays;

/**
 * Created by Keetmalin on 5/29/2017
 * Project - SemanticSimilarityMeasure - Copy
 */
public class TestRun {

    public static void main(String[] args) {

        //SET K VALUE AT THE BEGINNING
        int k = 20;
        Constants.setTermCount(k/Constants.L_GT_WORD_COUNT);

        //reading from CSV file
        CSV csvObject = new CSV();
        ValueMatrix[] valueMatrices = new ValueMatrix[100];
        NeuralNetwork neuralNetwork = null;

        try {
            //read value matrices for k values
            File file = new File("ValueMatrices/valueMatricesK"+k+".ser");
            if (file.exists()) {
                System.out.println("serialized file found. Reading from it");
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                valueMatrices = (ValueMatrix[]) in.readObject();
                in.close();
                fileIn.close();
            } else {
                System.out.println("Serialized value matrices object is missing");
            }

            //read NN object
            File file2 = new File("src/neuralNetwork.ser");
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file2);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                neuralNetwork = (NeuralNetwork) in.readObject();
                System.out.println("reading neuralNetwork object from file");
                in.close();
                fileIn.close();

                neuralNetwork.setInputVector(valueMatrices);
                neuralNetwork.TestNN(csvObject,k);



            } else {
                System.out.println("Serialized Neural Network not found");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //now run the tests

    }

   
}
