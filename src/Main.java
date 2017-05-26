/**
 * Created by Keetmalin on 5/25/2017.
 */

public class Main{

    public static void main(String[]args){

        //number of words under each term
        //make this l=10
        int l = 5;

        //reading from CSV file
        CSV csvObject = new CSV(l);

        //call word2vec model
        Word2vecModel word2vecModel = new Word2vecModel();

    }
}

