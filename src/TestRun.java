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

    //additional methods
    double calculatePrecision(CSV csvObject , MImatrix mImatrix, int p){

        return 1 - calculateError(csvObject, mImatrix, p);
    }

    double calculateRecall(CSV csvObject , Rmatrix rmatrix, int p){

        //this will keep track of G and W intersections
        int count = 0;
        String[] goldenStandardWords = csvObject.getWordList()[p].split(",");
        String[] rMatrixFirstRow = rmatrix.getRmatrix()[0];

        for (int i = 0 ; i < Constants.L_GT_WORD_COUNT ; i++){
            if (Arrays.asList(rMatrixFirstRow).contains(goldenStandardWords[i])){
                count++;
            }
        }

        return count/Constants.L_GT_WORD_COUNT;
    }

    double seek(String word, String[] array){

        if (Arrays.asList(array).indexOf(word) == -1){
            return array.length;
        } else{
            return (Arrays.asList(array).indexOf(word)) + 1;
        }
    }

    double calculateError(CSV csvObject , MImatrix miMatrix, int p){

        double tempSum = 0;
        String[] words=miMatrix.getWordArray();
        int count=0;
        String[] goldenStandardWords = csvObject.getWordList()[p].split(",");


        for (int i=0 ; i< Constants.L_GT_WORD_COUNT ; i++){

            double x = 0;
            String qWord=goldenStandardWords[i];
            double seekResult=seek(qWord ,words);

            if (seekResult < Constants.L_GT_WORD_COUNT){
                x = 1;
            } else{
                x = (words.length - seekResult)/ (words.length - Constants.L_GT_WORD_COUNT);
            }

            tempSum += x;
            if(seekResult<words.length){
                count++;
            }

        }

        if(count>0){
            return  1-((tempSum+Double.MIN_VALUE)/(count+Double.MIN_VALUE));
        }
        else{
            return 1;
        }

        //return 1-(tempSum/(Constants.L_GT_WORD_COUNT));
    }
}
