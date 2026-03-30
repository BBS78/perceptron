import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        ArrayList<Row> trainingSet = new ArrayList<Row>();
        ArrayList<Row> testSet = new ArrayList<Row>();

        double bias = 0;
        double a = 0.01;

        HashMap<String, Integer> answMap = new HashMap<String, Integer>();

        loop: while (true){
            System.out.println("\n==========================\nChose any option by entering the number: " +
                    "\n1. Input the training and test sets" +
                    "\n2. Train perceptron for n epochs" +
                    "\n3. Enter own vector" +
                    "\n input '0' to finish the program");
            System.out.print(">> ");
            Scanner s = new Scanner(System.in);
            int userInput = s.nextInt();
            System.out.println();


            switch(userInput){
                case 1:
                    System.out.print("Enter training set path: ");
                    Scanner sc = new Scanner(System.in);
                    String path = "src/"+sc.nextLine();
                    trainingSet = inputTheTrainingSet(path, answMap);
                    System.out.print("Enter test set path: ");
                    path = "src/"+sc.nextLine();
                    testSet = inputTheTrainingSet(path, answMap);
                    break;
                case 2:
                    System.out.print("Enter number of epochs: ");
                    Scanner sca = new Scanner(System.in);
                    int n = sca.nextInt();
                    train(
                            trainingSet,
                            weights,
                            bias,
                            a,
                            answMap
                    );
                    break;
                case 3:
                    ownVector();
                    break;
                case 0:
                    System.out.println("Finishing...");
                    break loop;
            }
        }

    }

    private static void ownVector() {
    }

    private static double[] train(ArrayList<Row> set, double[] w, double bias, double a, HashMap<String, Integer> answMap) {
        double[] newWeights = new double[w.length];
        double newBias;

        for (Row r : set){
            double[] x = r.attributes;
            double net = 0;
            for (int i = 0; i< x.length;i++){
                net += x[i]*w[i];
            }
            net -= bias;

            double y = 1;
            double d = answMap.get(r.classification);

            if (net < 0) y = 0;
            for (int i = 0; i< w.length; i++){
                // delta rule
                newWeights[i] = w[i] + a * (d - y) * x[i];
            }

        }
        return newWeights;


    }

    private static double[] generateWeights(int n){
        double[] output = new double[n];
        System.out.println("Random weights were generated:");
        for (int i = 0; i< output.length; i++){
            output[i] = Math.random();
            System.out.print(output[i] +" ");
        }
        return output;
    }

    private static ArrayList<Row> inputTheTrainingSet(String path, HashMap<String, Integer> answMap) {
        ArrayList<Row> output = new ArrayList<Row>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String l;
            while ((l = br.readLine()) != null){
                String[] row = l.split(",");
                Row r = new Row();
                r.attributes = new double[row.length-1];
                for (int i = 0; i < row.length; i++){
                    if (i<row.length-1){
                        r.attributes[i] = Double.parseDouble(row[i]);
                    }
                    else{
                        r.classification = row[i];
                        if (answMap.isEmpty()) answMap.put(row[i], 1);
                        answMap.putIfAbsent(row[i], 0);
                    }
                }
                System.out.println(r);
                output.add(r);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(output.size()+ " element(s) added to training set.");
        return output;
    }
}