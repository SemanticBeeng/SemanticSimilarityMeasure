/**
 * Created by Keetmalin on 5/25/2017
 * Project - SemanticSimilarityMeasure
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        //reading from CSV file
        CSV csvObject = new CSV();

//        ArrayList<ValueMatrix> valueMatrices = new ArrayList<ValueMatrix>();
//        ArrayList<Rmatrix> miMatrices = new ArrayList<Rmatrix>();

        ValueMatrix[] valueMatrices = new ValueMatrix[100];

        //call word2vec model
        Word2vecModel word2vecModel = new Word2vecModel();

        int i=0;
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
            if (Objects.equals(term, "lawyer")) break;

        }

        //Serialize the ValueMatrix Array to src
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("src/valueMatrices.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(valueMatrices);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in src/valueMatrices.ser");
        }catch(IOException exception) {
            exception.printStackTrace();
        }


        //initiate Neural Network
        NeuralNetwork neuralNetwork = new NeuralNetwork(Constants.NN_INPUT_LAYER, Constants.NN_HIDDEN_LAYER, Constants.NN_OUTPUT_LAYER);
        int maxRuns = 50000;
        double minErrorCondition = 0.001;
        neuralNetwork.setInputVector(valueMatrices);
        neuralNetwork.run(maxRuns, minErrorCondition,csvObject);



        //        //call neural network
//        NeuralNetwork nn = new NeuralNetwork(2, 4, 1);
//        int maxRuns = 50000;
//        double minErrorCondition = 0.001;
//        nn.run(maxRuns, minErrorCondition);

        //run( "judge","lawyer" );

    }

//    private static ILexicalDatabase db = new NictWordNet();
//    private static RelatednessCalculator[] rcs = {
//            new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db), new WuPalmer(db),
//            new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
//    };
//
//    private static void run(String word1, String word2) {
//        WS4JConfiguration.getInstance().setMFS(true);
//        for (RelatednessCalculator rc : rcs) {
//            double s = rc.calcRelatednessOfWords(word1, word2);
//            System.out.println(rc.getClass().getName() + "\t" + s);
//        }
//    }


}

