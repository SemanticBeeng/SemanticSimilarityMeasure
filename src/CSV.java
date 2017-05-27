import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Keetmalin on 5/25/2017.
 */
public class CSV {

    private String[] termList;
    private String[] wordList;

    public CSV() {

        //initialize arrays
        termList = new String[100];
        wordList = new String[100];

        //open CSV file & read from it
        try {
            Scanner scan = new Scanner(new File("CSV/lemmatizedGoldenStandard.csv"));
            int i = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] lineArray = line.split(",");

                termList[i] = lineArray[0];
                StringBuilder temp= new StringBuilder();
                for (int j=1; j< Constants.L_GT_WORD_COUNT+1; j++){
                    temp.append(lineArray[j]);
                    if (j!=Constants.L_GT_WORD_COUNT){
                        temp.append(",");
                    }
                }
                wordList[i] = temp.toString();
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }


    }

    public String[] getTermList() {
        return termList;
    }

    public String[] getWordList() {
        return wordList;
    }
}
