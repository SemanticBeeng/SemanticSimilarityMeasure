import java.io.Serializable;

/**
 * Created by Keetmalin on 5/26/2017
 * Project - SemanticSimilarityMeasure
 */
class Normalize implements Serializable{

    private Double[][] normalizedMatrix =  new Double[4][Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT];
    private double inf = Double.POSITIVE_INFINITY;
    private Double[][] matrix = new Double[4][Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT];

    private Double[] wup = {0.0 , 1.0};
    private Double[] jcn = {0.0 , inf};
    private Double[] hso = {0.0 , 16.0};
    private Double[] lesk = {0.0, inf};

    Normalize(Double[][] matrix){

        this.matrix = matrix;

        normalizeWUP();
        normalizeJCN();
        normalizeHSO();
        normalizeLESK();

    }

    private void normalizeWUP(){
        Double low = wup[0];
        Double high = wup[1];

        for (int i = 0 ; i<Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++){

            normalizedMatrix[0][i] = matrix[0][i];

        }
    }

    private void normalizeJCN (){
        Double low = jcn[0];
        Double high = jcn[1];

        for (int i = 0 ; i<Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++){

            Double temp = matrix[1][i];

            //normalize the value

            Double normalizedValue =( Math.exp(temp) - Math.exp(-temp))/(Math.exp(temp) + Math.exp(-temp));

            normalizedMatrix[1][i] = normalizedValue;

        }
    }

    private void normalizeHSO(){
        Double low = hso[0];
        Double high = hso[1];

        for (int i = 0 ; i<Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++){

            Double temp = matrix[2][i];
            Double m = 0.0625;

            //normalize the value

            Double normalizedValue = m * temp;

            normalizedMatrix[2][i] = normalizedValue;

        }
    }

    private void normalizeLESK(){
        Double low = lesk[0];
        Double high = lesk[1];

        for (int i = 0 ; i<Constants.C_CONSTANT * Constants.L_GT_WORD_COUNT ; i++){

            Double temp = matrix[3][i];

            //normalize the value

            Double normalizedValue =( Math.exp(temp) - Math.exp(-temp))/(Math.exp(temp) + Math.exp(-temp));

            normalizedMatrix[3][i] = normalizedValue;

        }
    }

    Double[][] getNormalizedMatrix(){
        return normalizedMatrix;
    }
}
