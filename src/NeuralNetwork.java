/**
 * Created by Keetmalin on 5/27/2017
 * Project - SemanticSimilarityMeasure
 */


import java.io.Serializable;
import java.text.*;
import java.util.*;

public class NeuralNetwork implements Serializable{
    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    final boolean isTrained = false;
    final DecimalFormat df;
    final Random rand = new Random();
    final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
    final Neuron bias = new Neuron();
    final int[] layers;
    final int randomWeightMultiplier = 1;
    MImatrix mim=null;

    final double epsilon = 0.00000000001;

    final double learningRate = 0.9f;
    final double momentum = 0.7f;

    // Inputs for xor problem
    ValueMatrix[] inputs;
    String[] validWords=null;

    // Corresponding outputs, xor training data
    final double expectedOutputs[][] = { { 0 }, { 1 }, { 1 }, { 0 } };
    double resultOutputs[][] = { { -1 }, { -1 }, { -1 }, { -1 } }; // dummy init
    double output[];

    // for weight update all
    final HashMap<String, Double> weightUpdate = new HashMap<String, Double>();

//    public static void main(String[] args) {
//        NeuralNetwork nn = new NeuralNetwork(2, 4, 1);
//        int maxRuns = 50000;
//        double minErrorCondition = 0.001;
//        nn.run(maxRuns, minErrorCondition);
//    }

    public NeuralNetwork(int input, int hidden, int output) {
        this.layers = new int[] { input, hidden, output };
        df = new DecimalFormat("#.0#");

        /**
         * Create all neurons and connections Connections are created in the
         * neuron class
         */
        for (int i = 0; i < layers.length; i++) {
            if (i == 0) { // input layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    inputLayer.add(neuron);
                }
            } else if (i == 1) { // hidden layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(inputLayer);
                    neuron.addBiasConnection(bias);
                    hiddenLayer.add(neuron);
                }
            }

            else if (i == 2) { // output layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(hiddenLayer);
                    neuron.addBiasConnection(bias);
                    outputLayer.add(neuron);
                }
            } else {
                System.out.println("!Error NeuralNetwork init");
            }
        }

        // initialize random weights
        for (Neuron neuron : hiddenLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : outputLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }

        // reset id counters
        Neuron.counter = 0;
        Connection.counter = 0;

        if (isTrained) {
            trainedWeights();
            updateAllWeights();
        }
    }

    // random
    double getRandom() {
        return randomWeightMultiplier * (rand.nextDouble() * 2 - 1); // [-1;1[
    }

    /**
     *
     * @param inputs
     *            There is equally many neurons in the input layer as there are
     *            in input variables
     */
    public void setInput(double inputs[]) {
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setOutput(inputs[i]);
        }
    }

    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }

    /**
     * Calculate the output of the neural network based on the input The forward
     * operation
     */
    public void activate() {
        for (Neuron n : hiddenLayer)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }

    /**
     * all output propagate back
     *
     * @param calculatedErrors
     *            first calculate the partial derivative of the error with
     *            respect to each of the weight leading into the output neurons
     *            bias is also updated here
     */
    public void applyBackpropagation(double calculatedErrors[]) {

        double errors[] =new double[calculatedErrors.length];

//        // error check, normalize value ]0;1[
//        for (int i = 0; i < expectedOutput.length; i++) {
//            double d = expectedOutput[i];
//            if (d < 0 || d > 1) {
//                if (d < 0)
//                    expectedOutput[i] = 0 + epsilon;
//                else
//                    expectedOutput[i] = 1 - epsilon;
//            }
//        }

        int i = 0;
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double ak = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double error = calculatedErrors[i];

                double partialDerivative = -ak * (1 - ak) * ai
                        * error;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }

        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double aj = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                    double wjk = out_neu.getConnection(n.id).getWeight();
//                    double desiredOutput = (double) expectedOutput[j];
                    double ak = out_neu.getOutput();
                    sumKoutputs = sumKoutputs+ (-(calculatedErrors[j]) * ak * (1 - ak) * wjk);
                    j++;
                }

                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
    }

    void setInputVector(ValueMatrix[] valueMatrices){

        this.inputs = valueMatrices;

    }

    double seek(String word, String[] array){

        if (Arrays.asList(array).indexOf(word) == -1){
            return array.length;
        } else{
            return (Arrays.asList(array).indexOf(word)) + 1;
        }
    }

    double calculateError(CSV csvObject ,  String[] words, int p){

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

    void TrainNN(int maxSteps, double minError, CSV csvObject) {
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;



        for (i = 0; i < maxSteps && error > minError; i++) {

            error = 0;
            for (int p = 0; p < inputs.length; p++) {

                double err = ActivateNN(csvObject, p,-1);

                applyBackpropagation(new double[]{err}); //Only one item in the array because the output layer has only one neuron
                error+=err;
            }
            error/=inputs.length;
            System.out.println("Epoch "+(i+1)+" out of "+maxSteps+" | Error ="+error+" (Target: "+minError+")");
        }

       // printResult();

        System.out.println("Training Error = " + error);
        System.out.println("##### EPOCH " + i+"\n");
        if (i == maxSteps) {
            System.out.println("!Error training try again");
        } else {
            printAllWeights();
            printWeightUpdate();
        }
    }

    public void TestNN(CSV csvObject,int k) {

        double error = 0;
        double recall = 0;


        for (int p = 0; p < inputs.length; p++) {

            double err = ActivateNN(csvObject, p,k);
            double rec=calculateRecall(csvObject,p);

            error += err;
            recall+=rec;
        }
        error /= inputs.length;


        System.out.println("Testing Error = " + error);
        System.out.println("Testing Recall = " + recall);

    }


    double calculateRecall(CSV csvObject , int p){

        //this will keep track of G and W intersections
        int count = 0;
        String[] goldenStandardWords = csvObject.getWordList()[p].split(",");




        for (int i = 0 ; i < Constants.L_GT_WORD_COUNT ; i++){
            if (Arrays.asList(validWords).contains(goldenStandardWords[i])){
                count++;
            }
        }

        return count/Constants.L_GT_WORD_COUNT;
    }




    private double ActivateNN(CSV csvObject, int p,int k) {
        double[][] values=inputs[p].getValueMatrixDouble();
        String[] words=inputs[p].getRmatrix().getRmatrix()[0];
        if(k<0){
            k=words.length;
        }
        mim=new MImatrix(words);

        for (int j = 0; j <values.length ; j++) {
            setInput(values[j]);
            activate();
            double op=getOutput()[0];
            if(Double.isNaN(op)){
                op=0;
            }
            mim.updateValueAt(j,getOutput()[0]); //update e_j. Use [0] directly because we know the output layer has only one item
        }
        //Now sort MImatrix
        //System.out.println();
        //System.out.println(mim.toString());

        mim.sort();

        // System.out.println(mim.toString());

        mim.buildWordArray();


        validWords=createPrunedArray(mim.getWordArray(),k);

        return calculateError(csvObject , validWords, p);
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

    void printResult()
    {
        System.out.println("NN example with xor training");
        for (int p = 0; p < inputs.length; p++) {
            System.out.print("INPUTS: ");
            for (int x = 0; x < layers[0]; x++) {
        //        System.out.print(inputs[p][x] + " ");
            }

            System.out.print("EXPECTED: ");
            for (int x = 0; x < layers[2]; x++) {
                System.out.print(expectedOutputs[p][x] + " ");
            }

            System.out.print("ACTUAL: ");
            for (int x = 0; x < layers[2]; x++) {
                System.out.print(resultOutputs[p][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    String weightKey(int neuronId, int conId) {
        return "N" + neuronId + "_C" + conId;
    }

    /**
     * Take from hash table and put into all weights
     */
    public void updateAllWeights() {
        // update weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
    }

    // trained data
    void trainedWeights() {
        weightUpdate.clear();

        weightUpdate.put(weightKey(3, 0), 1.03);
        weightUpdate.put(weightKey(3, 1), 1.13);
        weightUpdate.put(weightKey(3, 2), -.97);
        weightUpdate.put(weightKey(4, 3), 7.24);
        weightUpdate.put(weightKey(4, 4), -3.71);
        weightUpdate.put(weightKey(4, 5), -.51);
        weightUpdate.put(weightKey(5, 6), -3.28);
        weightUpdate.put(weightKey(5, 7), 7.29);
        weightUpdate.put(weightKey(5, 8), -.05);
        weightUpdate.put(weightKey(6, 9), 5.86);
        weightUpdate.put(weightKey(6, 10), 6.03);
        weightUpdate.put(weightKey(6, 11), .71);
        weightUpdate.put(weightKey(7, 12), 2.19);
        weightUpdate.put(weightKey(7, 13), -8.82);
        weightUpdate.put(weightKey(7, 14), -8.84);
        weightUpdate.put(weightKey(7, 15), 11.81);
        weightUpdate.put(weightKey(7, 16), .44);
    }

    public void printWeightUpdate() {
        System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        System.out.println();
    }

    public void printAllWeights() {
        System.out.println("printAllWeights");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        System.out.println();
    }
}