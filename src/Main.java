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

public class Main {

    public static void main(String[] args) {

//        //reading from CSV file
//        CSV csvObject = new CSV();
//
//        //call word2vec model
//        Word2vecModel word2vecModel = new Word2vecModel();
//
//        for (String term : csvObject.getTermList()) {
//
//            Matrix matrixObject = new Matrix(term, word2vecModel, csvObject);
//            Normalize normalize = new Normalize(matrixObject.getMatrix());
//            ValueMatrix valueMatrix = new ValueMatrix(term, csvObject, word2vecModel, normalize);
//
//
//        }


        run( "judge","lawyer" );

    }

    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = {
            new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db),
            new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
    };

    private static void run( String word1, String word2 ) {
        WS4JConfiguration.getInstance().setMFS(true);
        for ( RelatednessCalculator rc : rcs ) {
            double s = rc.calcRelatednessOfWords(word1, word2);
            System.out.println( rc.getClass().getName()+"\t"+s );
        }
    }


}

