import java.util.Arrays;

/**
 * Created by Nisansa on 5/29/2017.
 */
public class PrReMath {

    private static PrReMath math=new PrReMath();

    private PrReMath() {
    }

    public static PrReMath getInstance(){
        return math;
    }

    public double calculateRecall(CSV csvObject , String[] words, int p){

        //this will keep track of G and W intersections
        int count = 0;
        String[] goldenStandardWords = csvObject.getWordList()[p].split(",");




        for (int i = 0 ; i < Constants.L_GT_WORD_COUNT ; i++){
            if (Arrays.asList(words).contains(goldenStandardWords[i])){
                count++;
            }
        }

        return (double)count/ (double) Constants.L_GT_WORD_COUNT;
    }

    public double calculateError(CSV csvObject ,  String[] words, int p){

        double tempSum = 0;

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

    private double seek(String word, String[] array){

        if (Arrays.asList(array).indexOf(word) == -1){
            return array.length;
        } else{
            return (Arrays.asList(array).indexOf(word)) + 1;
        }
    }

}
