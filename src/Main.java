import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;



public class Main
{
    //The final array for the parallel sort
    public static ArrayList<Double> endArray;

    //The final array for the parallel sort

    public static ArrayList<Double> endSortArray;

    public static final ForkJoinPool pool = new ForkJoinPool();

    public static final ArrayList<Double> initialArray = new ArrayList<>();

    public static int sampleSize;

    public static final ArrayList<Double> times = new ArrayList<>(20);

    public static void parallelRun(String outputFile) throws IOException
    {
        //parallel run
        for (int run = 1; run < 21; run++) //algorithm will run 20 times
        {
            long startTime = System.nanoTime();

            pool.invoke(new Filt(sampleSize / 2,
                    initialArray.size() - (sampleSize / 2)));

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

    public static void serialRun(ArrayList<Double> endSortArray)
    {
        for (int run = 1; run < 21; run++) //algorithm will run 20 times
        {
            long startTime = System.nanoTime();

            //Arrays.sort(endSortArray);
            Filt.seqFilter();

            for (int i = initialArray.size() - (sampleSize / 2); i < initialArray.size(); i++)
            {
                endSortArray.set(i, initialArray.get(i));
            }
            //ollections.sort(endSortArray);

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

        System.out.println("Adjusted serial run average:  " + average + " milliseconds.");
        for (int i = 0; i < initialArray.size(); i++)
        {
           System.out.println((i + 1) + " " + endSortArray.get(i));
        }
        System.out.println("Finished!");

    }

    public static void main(String[] args) throws Exception
    {
        Scanner scan = new Scanner(System.in);

        File inputFile = new File(args[0]);
        sampleSize = Integer.parseInt(args[1]);


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
        endSortArray = new ArrayList<>(initialArray);



        //The initial elements outside the borders are added to finalArray
        for (int i = 0; i < sampleSize / 2; i++)
        {
            endArray.set(i, initialArray.get(i));
        }

        //int numP2 = Runtime.getRuntime().availableProcessors();


        //Filtering will run within the borders
        Filt filt = new Filt(sampleSize / 2,
                initialArray.size() - (sampleSize / 2));


        int numP = Runtime.getRuntime().availableProcessors();
        int numP2 = Runtime.getRuntime().availableProcessors();
        int numP3 = Runtime.getRuntime().availableProcessors();
        int numP4 = Runtime.getRuntime().availableProcessors();
        int numP5 = Runtime.getRuntime().availableProcessors();

        parallelRun(args[2]);

        serialRun(endSortArray);




        //Collections.sort(initialArray);

        //System.out.println("Adjusted parallel (Serial framework) run average:  " + average + " milliseconds.");



    }
}