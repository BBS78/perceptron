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

        Perceptron perceptron = null;

        double a = 0.01;

        HashMap<String, Integer> answMap = new HashMap<String, Integer>();
        HashMap<Integer, String> revAnswMap = new HashMap<Integer, String>();

        loop: while (true){
            System.out.println("\n==========================\nChose any option by entering the number: " +
                    "\n1. Input the training and test sets" +
                    "\n2. Train perceptron for n epochs" +
                    "\n3. Test perceptron" +
                    "\n4. Change learning rate" +
                    "\n5. Enter own vector" +
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
                    trainingSet = inputSet(path, answMap, revAnswMap);
                    System.out.print("Enter test set path: ");
                    path = "src/"+sc.nextLine();
                    testSet = inputSet(path, answMap, revAnswMap);
                    break;
                case 2:
                    if (perceptron == null) perceptron = new Perceptron(trainingSet.getFirst().attributes.length, a);

                    System.out.print("Enter number of epochs: ");
                    Scanner sca = new Scanner(System.in);
                    int n = sca.nextInt();

                    ArrayList<Integer> lastErrors = new ArrayList<>();

                    for (int i = 0; i < n; i++) {
                        int errCounter = 0;

                        for (Row r : trainingSet) {
                            int d = answMap.get(r.classification);
                            int y = perceptron.predict(r.attributes);

                            if (y != d) errCounter++;

                            perceptron.train(r.attributes, d);
                        }

                        System.out.println("Epoch " + (i + 1) + ": " + errCounter + " errors");

                        lastErrors.add(errCounter);

                        if (lastErrors.size() > 6) {
                            lastErrors.removeFirst();
                        }

                        if (lastErrors.size() == 6) {
                            boolean same = true;
                            int first = lastErrors.getFirst();

                            for (int j = 1; j < lastErrors.size(); j++) {
                                if (lastErrors.get(j) != first) {
                                    same = false;
                                    break;
                                }
                            }

//                            if (same) {
//                                double currA = perceptron.getA();
//                                currA += 0.01;
//                                perceptron.setA(currA);
//                                System.out.println("Errors did not change for 6 epochs. Learning rate increased to: " + currA);
//                                lastErrors.clear();
//                            }
                        }
                    }
                    break;
                case 3:
                    int corrCounter = 0;
                    for (Row r : testSet){
                        int d = answMap.get(r.classification);

                        int y = perceptron.predict(r.attributes);
                        String answ = revAnswMap.get(y);

                        if (d==y){
                            corrCounter++;
                            System.out.println();
                        }
                    }

                    double accuracy = 100.0 * corrCounter / testSet.size();
                    System.out.println("\nAccuracy: " + accuracy + "%");
                    break;
                case 4:
                    System.out.println("Current learning rate: " + perceptron.getA());
                    System.out.print("Enter new value: ");
                    Scanner scan = new Scanner(System.in);
                    double newA = scan.nextDouble();
                    perceptron.setA(newA);
                    System.out.println("Learning rate was set to " + newA);
                    break;
                case 5:
                    System.out.println("Enter vector values separated by commas:");
                    System.out.print(">>> ");
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();

                    String[] parts = line.split(",");
                    double[] vector = new double[parts.length];

                    for (int i = 0; i < parts.length; i++) {
                        vector[i] = Double.parseDouble(parts[i].trim());
                    }

                    int prediction = perceptron.predict(vector);
                    String predictedClass = revAnswMap.get(prediction);

                    System.out.println("Predicted class: " + predictedClass);
                    break;

                case 0:
                    System.out.println("Finishing...");
                    break loop;
            }
        }

    }

    private static ArrayList<Row> inputSet(String path, HashMap<String, Integer> answMap, HashMap<Integer, String> revAnswMap) {
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

                        if (!answMap.containsKey(r.classification)) {
                            int value = answMap.size();
                            answMap.put(r.classification, value);
                            revAnswMap.put(value, r.classification);
                        }
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