import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.RecursiveAction;

//public class SumAction extends RecursiveAction {
    // ...

    /*@Override
    protected void compute() {
        if (data.size() <= 100) { // base case
            long sum = computeSumDirectly();
            System.out.format("Sum of %s: %d\n", data.toString(), sum);
        } else { // recursive case
            // Calcuate new range
            int mid = data.size() / 2;
            SumAction firstSubtask =
                    new SumAction(data.subList(0, mid));
            SumAction secondSubtask =
                    new SumAction(data.subList(mid, data.size()));

            firstSubtask.fork(); // queue the first task
            secondSubtask.compute(); // compute the second task
            firstSubtask.join(); // wait for the first task result

            // Or simply call
            //invokeAll(firstSubtask, secondSubtask);
        }
    }
}*/


    public class FilterObject extends RecursiveAction {


        private int lower;

        private int upper;

        public FilterObject(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        @Override

        public void compute() {
            if (upper - lower <= 1000) {
                for (int i = lower; i < upper; i++) {
                    ArrayList<Double> window = new ArrayList<>();

                    for (int k = i - (Main.sampleSize / 2); k <= i + (Main.sampleSize / 2); k++) {
                        window.add(Main.initialArray.get(k));
                    }

                    Main.endArray.set(i, getMedian(window));

                }
            } else {
                FilterObject subStream1 = new FilterObject(lower, (upper + lower) / 2);
                FilterObject subStream2 = new FilterObject((upper + lower) / 2, upper);
                //nvokeAll(substream1, substream2);
                subStream1.fork(); // queue the first task
                subStream2.compute(); // compute the second task
                subStream1.join(); // wait for the first task result
            }

        }


        private static double getMedian(ArrayList<Double> array){
            Collections.sort(array);
            return array.get(Main.sampleSize / 2);
        }
    }

