import java.util.Arrays;

/**
 * Created by Keetmalin on 5/26/2017
 * Project - SemanticSimilarityMeasure
 */
public class ValueMatrix {

    private Double[][] valueMatrix = new Double[Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT][5];
    private double[][] valueMatrixDouble = new double[Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT][5];
    private Normalize normalize;
    private Word2vecModel word2vecModel;
    private MIMatrix miMatrix;

    ValueMatrix(String term, CSV csvObject, Word2vecModel word2vecModel, Normalize normalize){

        int index = Arrays.asList(csvObject.getTermList()).indexOf(term);
        this.word2vecModel = word2vecModel;
        this.normalize = normalize;

        createMatrix(index);
    }

    private void createMatrix(int index){

            for (int i=0 ; i <Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++ ){
                valueMatrix[i][0] = Double.parseDouble(word2vecModel.getOutputDistanceList(index)[i]);
                valueMatrix[i][1] = normalize.getNormalizedMatrix()[0][i];
                valueMatrix[i][2] = normalize.getNormalizedMatrix()[1][i];
                valueMatrix[i][3] = normalize.getNormalizedMatrix()[2][i];
                valueMatrix[i][4] = normalize.getNormalizedMatrix()[3][i];

                valueMatrixDouble[i][0] = Double.parseDouble(word2vecModel.getOutputDistanceList(index)[i]);
                valueMatrixDouble[i][1] = normalize.getNormalizedMatrix()[0][i];
                valueMatrixDouble[i][2] = normalize.getNormalizedMatrix()[1][i];
                valueMatrixDouble[i][3] = normalize.getNormalizedMatrix()[2][i];
                valueMatrixDouble[i][4] = normalize.getNormalizedMatrix()[3][i];
            }



    }

    public void setMiMatrix(MIMatrix miMatrix){
        this.miMatrix = miMatrix;
    }

    public Double[][] getValueMatrix(){
        return valueMatrix;
    }

    public double[][] getValueMatrixDouble(){

        return valueMatrixDouble;

    }




}
