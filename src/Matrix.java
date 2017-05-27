import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.Arrays;

/**
 * Created by Keetmalin on 5/26/2017
 * Project - SemanticSimilarityMeasure
 */
class Matrix {

    private static ILexicalDatabase db = new NictWordNet();
    private Double[][] matrix = new Double[4][Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT];
    private Word2vecModel word2vecModel;
    private CSV csvObject;

    Matrix(String term, Word2vecModel word2vecModel, CSV csvObject) {

        WS4JConfiguration.getInstance().setMFS(true);

        RelatednessCalculator[] rcs = {
                new WuPalmer(db),
                new JiangConrath(db),
                new HirstStOnge(db),
                new Lesk(db)
        };

        this.csvObject = csvObject;
        this.word2vecModel = word2vecModel;

        fillMatrix(rcs, term);


    }


    private void fillMatrix(RelatednessCalculator[] rcs, String term) {

        int i = 0;
        int index = Arrays.asList(csvObject.getTermList()).indexOf(term);
        for (String word : word2vecModel.getOutputWordList(index)) {

            int j = 0;
            for (RelatednessCalculator rc : rcs) {

                double s = rc.calcRelatednessOfWords(term, word);

                matrix[j][i] = s;
                j++;
            }
            i++;
        }


    }

    Double[][] getMatrix(){
        return matrix;
    }
}
