import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;


public class Main
{

    public static final ForkJoinPool pool = new ForkJoinPool();

    public static final ArrayList<Double> initialArray = new ArrayList<>();

    public static ArrayList<Double> endArray;

    public static int sampleSize;

    public static void main(String[] args) throws Exception
    {
        Scanner scan = new Scanner(System.in);

        File inputFile = new File(args[0]);
        sampleSize = Integer.parseInt(args[1]);
        File outputFile = new File(args[2]);

        //Input validation
        /*while (filterSize % 2 == 0 || filterSize < 3 || filterSize > 21)
        {
            System.out.println("Your filter size must be odd and "
                    + "range from 3 to 21 inclusive.");

            System.out.print("Submit input in the following form:"
                    + "\n<input file> <filter size> <output file>\n>>> ");
            inputFile = new File(scan.next());
            filterSize = scan.nextInt();
            outputFile = new File(scan.next());
        }*/


        //User chooses from 1 of 3 impementations
        /*System.out.print("Method?\n(1) Sequential\n(2) Parallel (ForkJoin "
                + "Framework)\n(3) Parallel (Standard Threads)\nType '1', '2', "
                + "or '3'\n>>> ");
        int method = scan.nextInt();*/

        //open The file
        scan = new Scanner(inputFile);

        int sampleSize = scan.nextInt();

        for (int i = 0; i < sampleSize; i++)
        {
            //Scan the number of the sample size
            scan.nextInt();

            double nextDouble = scan.nextDouble();
            initialArray.add(nextDouble);
        }

        endArray = new ArrayList<>(initialArray);

        //The initial elements outside the borders are added to finalArray
        for (int i = 0; i < sampleSize / 2; i++)
        {
            endArray.set(i, initialArray.get(i));
        }

        //Filtering will run within the borders
        FilterObject filt = new FilterObject(sampleSize / 2,
                initialArray.size() - (sampleSize / 2));

        ArrayList<Double> times = new ArrayList<>(20);//to calculate average run time
        int numP = Runtime.getRuntime().availableProcessors();

        for (int run = 1; run < 21; run++) //algorithm will run 20 times
        {
            long startTime = System.nanoTime();

            pool.invoke(new FilterObject(sampleSize / 2,
                    initialArray.size() - (sampleSize / 2)));

            /*switch (method)
            {
                case 1: //Sequential
                    filt.seqFilter();
                    break;
                case 2: //ForkJoin Framework
                    pool.invoke(new FilterObject(sampleSize / 2,
                            startArray.size() - (sampleSize / 2)); //anonymous object pass
                    break;
            }*/

            //The last few elements outside the borders are added to finalArray
            for (int i = initialArray.size() - (sampleSize / 2); i < initialArray.size(); i++)
            {
                endArray.set(i, initialArray.get(i));
            }

            //Calculates time elapsed per run
            Double timeElapsed = (System.nanoTime() - startTime) / 1000000.0;
            times.add(run - 1, timeElapsed);
            System.out.println("Run " + run + ": " + timeElapsed + " milliseconds.");
        }

        //Calculates average run time over 20 runs after removing highest value
        times.remove(Collections.max(times));
        double timesSum = 0;
        for (Double e : times)
        {
            timesSum += e;
        }
        double average = timesSum / 19;

        System.out.println("Adjusted parallel (ForkJoin framework) run average:  " + average + " milliseconds.");

        //All that follows is for printing to the output file.
        PrintStream outStream = new PrintStream(outputFile);

        System.out.print("Printing to file...");
        //Size of the file and all values are printed to file
        outStream.println(initialArray.size());

        for (int i = 0; i < initialArray.size(); i++)
        {
            outStream.println((i + 1) + " " + endArray.get(i));
        }
        System.out.println("Finished!");
    }
}