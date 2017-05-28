/**
 * Created by Keetmalin on 5/25/2017
 * Project - SemanticSimilarityMeasure
 */

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        //reading from CSV file
        CSV csvObject = new CSV();

//        ArrayList<ValueMatrix> valueMatrices = new ArrayList<ValueMatrix>();
        ArrayList<MIMatrix> miMatrices = new ArrayList<MIMatrix>();

        ValueMatrix[] valueMatrices = new ValueMatrix[100];

        //call word2vec model
        Word2vecModel word2vecModel = new Word2vecModel();

        int i=0;
        for (String term : csvObject.getTermList()) {

            Matrix matrixObject = new Matrix(term, word2vecModel, csvObject);
            Normalize normalize = new Normalize(matrixObject.getMatrix());
            ValueMatrix valueMatrix = new ValueMatrix(term, csvObject, word2vecModel, normalize);
            MIMatrix miMatrix = new MIMatrix(term, csvObject, word2vecModel);

            valueMatrices[i] = valueMatrix;
            miMatrices.add(miMatrix);
            System.out.println("Value Matrix for term = " + term + " , done");

            i++;


            //delete this. This is to avoid the for loop from calculating all 100 terms. This will skip after the first term "layer"
            if (Objects.equals(term, "lawyer")) break;

        }
        //initiate Neural Network
        NeuralNetwork neuralNetwork = new NeuralNetwork(Constants.NN_INPUT_LAYER, Constants.NN_HIDDEN_LAYER, Constants.NN_OUTPUT_LAYER);
        int maxRuns = 50000;
        double minErrorCondition = 0.001;
        neuralNetwork.setInputVector(valueMatrices);
        neuralNetwork.run(maxRuns, minErrorCondition);



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

