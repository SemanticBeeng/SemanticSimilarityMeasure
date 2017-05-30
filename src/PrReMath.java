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

        double tempSumG = 0;
        double tempSumNG = 0;

        int countG=0;
        int countNG=0;
        String[] goldenStandardWords = csvObject.getWordList()[p].split(",");


//        for (int i=0 ; i< Constants.L_GT_WORD_COUNT ; i++){
//
//            double x = 0;
//            String qWord=goldenStandardWords[i];
//            double seekResult=seek(qWord ,words);
//
//            if (seekResult < Constants.L_GT_WORD_COUNT){
//                x = 1;
//            } else{
//                x = (words.length - seekResult)/ (words.length - Constants.L_GT_WORD_COUNT);
//            }
//
//            tempSum += x;
//            if(seekResult<words.length){
//                count++;
//            }
//
//        }


        for (int i = 0; i <words.length ; i++) {
            double x = 0;
            String qWord=words[i];
            double seekResultG=seek(qWord ,goldenStandardWords); //see if the word is a golden word
            double seekResult=seek(qWord ,words);

            if(seekResultG<Constants.L_GT_WORD_COUNT){ //It is a golden word
                if (seekResult < Constants.L_GT_WORD_COUNT){
                    x = 1; //Reward for being in golden range
                } else{
                    x = (words.length - seekResult)/ (words.length - Constants.L_GT_WORD_COUNT); //penalize for being outside golden range
                }
                tempSumG += x;
                countG++;
            }
            else{
                if (seekResult < Constants.L_GT_WORD_COUNT){
                    x = 0; //Penalize for being in golden range
                } else{
                    x = 1; //-((words.length - seekResult)/ (words.length - Constants.L_GT_WORD_COUNT)); //reward for being out side of golden range
                }
                tempSumNG += x;
                countNG++;
            }



        }
        double errG=0;
        double errNG=0;




        if(countG>0){
            errG=  1-((tempSumG+Double.MIN_VALUE)/(countG+Double.MIN_VALUE));
        }
        else{
            errG= 1;
        }

        if(countNG>0){
            errNG=  1-((tempSumNG+Double.MIN_VALUE)/(countNG+Double.MIN_VALUE));
        }
        else{
            errNG= 1;
        }
        return (errG+errNG)/2;

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
