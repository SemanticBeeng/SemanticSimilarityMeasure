/**
 * Created by Nisansa on 5/29/2017.
 */
public class Word2VecEvaluator {

    public static void main(String[] args) {
        Word2VecEvaluator w2ve=new Word2VecEvaluator();
        w2ve.run();
    }

    private void run(){
        //Loop for G, LR and LL files one by one {  //Keet must update this
            String fielname=""; //G, LR and LL files //Keet must update this
            CSV csvObject=null; //Load this //Keet must update this
            String[] allWords=null; //This is the 0th line of rMatrix //Keet must update this
            int numOfInptLines=0;  //Keet must update this


            double error=0;
            double recall=0;

            int[] K={20,50,100,200,500};

            for (int k:K) {
                String[] validWords= createPrunedArray(allWords, k);
                for (int p = 0; p < numOfInptLines; p++) {
                    PrReMath prMath = PrReMath.getInstance();
                    double err = prMath.calculateError(csvObject, validWords, p);
                    double rec = prMath.calculateRecall(csvObject, validWords, p);
                    error += err;
                    recall += rec;
                }
                error /= numOfInptLines;
                recall /= numOfInptLines;

                System.out.println(fielname);
                System.out.println("Error = " + error);
                System.out.println("Recall = " + recall);
            }
        //}  //uncomment //Keet must update this
    }

    private String[] createPrunedArray(String[] original,int k){
        String[] newArray=new String[k];
        for (int i = 0; i <k ; i++) {
            if(i<original.length){
                newArray[i]=original[i];
            }
            else{
                newArray[i]=""; //Just in case
            }
        }


        return newArray;
    }
}
