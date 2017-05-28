import java.util.Arrays;

/**
 * Created by Keetmalin on 5/27/2017
 * Project - SemanticSimilarityMeasure
 */
public class Rmatrix {

    private String[][] rMatrix = new String[2][Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ];
    private CSV csvObject;
    private Word2vecModel word2vecModel;

    public Rmatrix(String term , CSV csvObject, Word2vecModel word2vecModel){

        this.csvObject = csvObject;
        this.word2vecModel = word2vecModel;

        int index = Arrays.asList(csvObject.getTermList()).indexOf(term);

        for (int i =0 ; i <Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++ ){
            rMatrix[0][i] = word2vecModel.getOutputWordList(index)[i];
        }

        for (int i =0 ; i <Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++ ){
            rMatrix[1][i] = "0.0";
        }


    }

    public void setERow(String[] outputArray){
        for (int i =0 ; i <Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++ ){
            rMatrix[1][i] = outputArray[i];
        }
    }

    public String[][] getRmatrix(){
        return rMatrix;
    }




}
