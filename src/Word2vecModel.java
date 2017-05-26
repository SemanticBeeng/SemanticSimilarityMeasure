import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Keetmalin on 5/25/2017.
 */
public class Word2vecModel {

    //use C=20 and l=10
    private String[] returnedWords = new String[Constants.TERM_COUNT];

    public Word2vecModel(){

        //open CSV file & read from it
        try {
            Scanner scan = new Scanner(new File("CSV/word2vecoutput.csv"));
            int i = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                returnedWords[i] = line;

                //store each value and cosine distance in two arrays
                //keet:0.123,leet:0.1234,.....
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

    }

    public String[] getOutputWordList(int i){

        String[] returnArray = new String[Constants.C_CONSTANT*Constants.L_GT_WORD_COUNT];
        String[] tempArray = returnedWords[i].split(",");

        for (int j=0 ; j<Constants.C_CONSTANT*Constants.L_GT_WORD_COUNT ; j++){
            returnArray[j] = tempArray[j].split(":")[0];
        }

        return returnArray;
    }


    public String[] getOutputDistanceList(int i){

        String[] returnArray = new String[200];
        String[] tempArray = returnedWords[i].split(",");

        for (int j=0 ; j<200 ; j++){
            returnArray[j] = tempArray[j].split(":")[1];
        }

        return returnArray;
    }
}

