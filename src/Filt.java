import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.RecursiveAction;

public class Filt extends RecursiveAction {


        private static int lower;

        private static int upper;

        public Filt(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        @Override

        public void compute()
        {
            if (upper - lower <= 1000) {
                for (int i = lower; i < upper; i++) {
                    ArrayList<Double> window = new ArrayList<>();

                    for (int k = i - (Main.sampleSize / 2); k <= i + (Main.sampleSize / 2); k++) {
                        window.add(Main.initialArray.get(k));
                    }

                    Main.endArray.set(i, getMedian(window));

                }
            } else {
                Filt subStream1 = new Filt(lower, (upper + lower) / 2);
                Filt subStream2 = new Filt((upper + lower) / 2, upper);
                //nvokeAll(substream1, substream2);
                subStream1.fork(); // queue the first task
                subStream2.compute(); // compute the second task
                subStream1.join(); // wait for the first task result
            }

        }

        public static void seqFilter()
        {
        for (int i = lower; i < upper; i++)
        {
            ArrayList<Double> window = new ArrayList<>();

            //Iterates through window
            for (int k = i - (Main.sampleSize / 2); k <= i + (Main.sampleSize / 2); k++)
            {
                window.add(Main.initialArray.get(k));
            }

            Main.endSortArray.set(i, getMedian(window));

        }
    }

        private static double getMedian(ArrayList<Double> array){
            Collections.sort(array);
            return array.get(Main.sampleSize / 2);
        }
}

