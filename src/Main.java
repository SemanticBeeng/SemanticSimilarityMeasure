import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

/**
 * Created by Keetmalin on 5/25/2017.
 */

public class Main{

    public static void main(String[]args){

        //reading from CSV file
        CSV csvObject = new CSV();

        //call word2vec model
        Word2vecModel word2vecModel = new Word2vecModel();

        for ( String term: csvObject.getTermList()) {

            Matrix matrixObject = new Matrix(term, word2vecModel, csvObject);
            Normalize normalize = new Normalize(matrixObject.getMatrix());


        }


    }



}

