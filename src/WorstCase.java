import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class WorstCase {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    /* define constants */
    static int MAXINPUTSIZE  = 2000;
    //static int MAXINPUTSIZE  = 1000;

    private static String result;
    private final int length;
    static String ResultsFolderPath = "/home/nicolocker/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;


    public static void main(String[] args) {

        verifyWorks();
        System.out.println("\n");

        System.out.println("Running first full experiment...");
        runFullExperiment("LCSubstringWorst-Exp1.txt");
        System.out.println("Running second full experiment...");
        runFullExperiment("LCSubstringWorst-Exp2.txt");
        System.out.println("Running third full experiment...");
        runFullExperiment("LCSubstringWorst-Exp3.txt");
    }

    public static void verifyWorks(){
        System.out.println("\n------- Test Run 1 -----");
        String a1 = "AAAAAAAAAAAAAAAAA";
        String b1 = "AAAAAAAAAAAAAAAAA";
        WorstCase.search(a1,b1);
    }


    public static void runFullExperiment(String resultsFileName) {

        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);
        } catch (Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file " + ResultsFolderPath + resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...
        }

        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial

        resultsWriter.println("#X(input size)         N(result size)            T(avg time)"); // # marks a comment in gnuplot data
        resultsWriter.flush();
        /* for each size of input we want to test: in this case starting small and doubling the size each time */

        String a = "";
        String b = "";

        for (int inputSize = 100; inputSize < MAXINPUTSIZE; inputSize = inputSize + 100) {
            // progress message...
            System.out.println("Running test for input size " + inputSize + " ... ");

            long batchElapsedTime = 0;

            /* force garbage collection before each batch of trials run so it is not included in the time */

            System.gc();
            a = generateString(inputSize);
            System.out.println(a);
            b = generateString(inputSize);
            System.out.println(b);

            // run the trials
            //for (long trial = 0; trial < numberOfTrials; trial++) {

            TrialStopwatch.start(); // *** uncomment this line if timing trials individually

            /* run the function we're testing on the trial input */
            WorstCase.search(a,b);


            batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually
            // }

            // double averageTimePerTrialInBatch = (double) batchElapsedTime / (double) numberOfTrials; // calculate the average time per trial in this batch

            double averageTimePerTrialInBatch = (double) batchElapsedTime;

            /* print data for this size of input */
            resultsWriter.printf("%6d  %20d %30.2f\n", inputSize, result.length(), averageTimePerTrialInBatch); // might as well make the columns look nice
            resultsWriter.flush();
            System.out.println(" ....done.");
        }
    }


    public static WorstCase search(String str1, String str2){

        int length1 = str1.length();
        int length2 = str2.length();
        String result = "";

        for( int i = 0; i < length1; i++){
            for( int j = 0; j < length2; j++){
                for( int k = 0; i + k < length1 && j + k < length2; k++){
                    if( str1.charAt(i + k) != str2.charAt(j + k)){
                        break;
                    }
                    if( k + 1 > result.length()){
                        result = str1.substring(i, i + k + 1);

                    }
                }
            }
        }
        System.out.println("Common Substring - " + result);
        System.out.println("Length of Common SubString - " + result.length());

        return new WorstCase(result, result.length());
    }

    // constructor
    public WorstCase(String result, int length) {
        this.result = result;
        this.length = length;
    }

    //https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
    public static String generateString(int stringSize){
        String string = "A";

        StringBuilder stringBuffer = new StringBuilder(stringSize);

        for( int i = 0; i < stringSize; i++){
            int index = (int)(string.length() * Math.random());
            stringBuffer.append(string.charAt(index));
        }
        return stringBuffer.toString();
    }
}

