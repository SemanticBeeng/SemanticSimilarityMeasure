import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Nisansa on 5/27/2017.
 */
public class MImatrix {
    private ArrayList<wordItem> wordItems=new ArrayList<>();
    String[] words=null;

    public static void main(String[] args) {
        MImatrix mim = new MImatrix(new String[]{"a", "b"});
        mim.updateValueAt(1, 0.5);
        System.out.println(mim);
        Collections.sort(mim.getWordItems());
        System.out.println(mim);
    }


    public MImatrix(String[] words) {
        for (int i = 0; i <words.length ; i++) {
            wordItem wi=new wordItem(words[i]);
            getWordItems().add(wi);
        }
    }

    public void updateValueAt(int i,double d){
        getWordItems().get(i).setVal(d);
    }

    public void buildWordArray(){
        String[] words=new String[wordItems.size()];
        for (int i = 0; i <wordItems.size() ; i++) {
            words[i]=wordItems.get(i).getWord();
        }
    }


    public String[] getWordArray(){
       return words;
    }


    public String toString(){
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        for (int i = 0; i < getWordItems().size() ; i++) {
            wordItem wi= getWordItems().get(i);
            sb1.append(wi.getWord());
            sb1.append("\t");
            sb2.append(wi.getVal());
            sb2.append("\t");
        }
        sb1.append("\n");
        sb1.append(sb2.toString());
        return sb1.toString();
    }

    public ArrayList<wordItem> getWordItems() {
        return wordItems;
    }



    class wordItem implements Comparable{
        private String word="";
        private double val=0;


        public wordItem(String word) {
            this.word = word;
        }

        public String getWord() {
            return word;
        }


        public double getVal() {
            return val;
        }

        public void setVal(double val) {
            this.val = val;
        }

        @Override
        public int compareTo(Object o) {
            wordItem w=(wordItem)o;
            if(this.val>w.getVal()){
                return -1;
            }
            else if(this.val<w.getVal()){
                return 1;
            }
            return 0;
        }
    }


}
