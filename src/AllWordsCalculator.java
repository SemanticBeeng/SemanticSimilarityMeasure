import java.io.*;

/**
 * Created by Keetmalin on 5/30/2017
 * Project - SemanticSimilarityMeasure
 */
public class AllWordsCalculator {

    //use C=20 and l=10
    private String[] returnedWords = new String[Constants.TERM_COUNT];

    AllWordsCalculator(String filename){

        //open CSV file & read from it
        try {
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            int i = 0;
            while((line = br.readLine()) != null) {
                returnedWords[i] = line;


                //store each value and cosine distance in two arrays
                //keet:0.123,leet:0.1234,.....
                i++;
            }

//
//            Scanner scan = new Scanner(new File("CSV/Word2VecResult.csv"));
//            int i = 0;
//            while (scan.hasNextLine()) {
//                String line = scan.nextLine();
//
//                returnedWords[i] = line;
//
//                //store each value and cosine distance in two arrays
//                //keet:0.123,leet:0.1234,.....
//                i++;
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String[] getOutputWordList(int i){

        String[] returnArray = new String[Constants.C_CONSTANT*Constants.L_GT_WORD_COUNT];
        String[] tempArray = returnedWords[i].split(",");

        for (int j=0 ; j<Constants.C_CONSTANT*Constants.L_GT_WORD_COUNT ; j++){
            returnArray[j] = tempArray[j].split(":")[0];
        }

        return returnArray;
    }


    String[] getOutputDistanceList(int i){

        String[] returnArray = new String[Constants.C_CONSTANT*Constants.L_GT_WORD_COUNT];
        String[] tempArray = returnedWords[i].split(",");

        for (int j=0 ; j<Constants.C_CONSTANT*Constants.L_GT_WORD_COUNT ; j++){
            returnArray[j] = tempArray[j].split(":")[1];
        }

        return returnArray;
    }
}
