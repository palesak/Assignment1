import java.util.*;

public class PCP {

    public class SumArray extends RecursiveTask<Integer> {
        int lo; // arguments
        int hi;
        int[] arr;
        static final int SEQUENTIAL_CUTOFF = 500;

        int ans = 0; // result

        SumArray(int[] a, int l, int h) {
            lo = l;
            hi = h;
            arr = a;
        }


        protected Integer compute() {// return answer - instead of run
            if ((hi - lo) < SEQUENTIAL_CUTOFF) {
                int ans = 0;
                for (int i = lo; i < hi; i++)
                    ans += arr[i];
                return ans;
            } else {
                SumArray left = new SumArray(arr, lo, (hi + lo) / 2);
                SumArray right = new SumArray(arr, (hi + lo) / 2, hi);

                // order of next 4 lines
                // essential  why?
                left.fork();
                int rightAns = right.compute();
                int leftAns = left.join();
                return leftAns + rightAns;
            }
        }
    }

    //public class SumAll {
        static long startTime = 0;

        private static void tick() {
            startTime = System.nanoTime();
        }

        private static float tock() {
            return (System.nanoTime() - startTime) / 1000000.0f;
        }

        static final ForkJoinPool fjPool = new ForkJoinPool();

        static int sum(int[] arr) {
            return fjPool.invoke(new SumArray(arr, 0, arr.length));
        }

        public static void main(String[] args) {
            int max = 1000000;
            int[] arr = new int[max];
            for (int i = 0; i < max; i++) {
                arr[i] = 100;
            }
            tick();
            int sumArr = sum(arr);
            float time = tock();
            System.out.println("Run took " + time + " milliseconds");

            System.out.println("Sum is:");
            System.out.println(sumArr);
            tick();
            sumArr = sum(arr);
            time = tock();
            System.out.println("Second run took " + time + " milliseconds");

            System.out.println("Sum is:");
            System.out.println(sumArr);

        }

    //}
}