import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Question2 {
    static int[] values = new int[200000000];
    static long[] portionSum = new long[4];
    static int portionSize = values.length/4;
    public static void main(String[] args) throws InterruptedException {
        generateValues();

        long startTime = System.currentTimeMillis();
        long sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Serial total sum = " + sum);
        System.out.println("Serial total time = " + (endTime - startTime));


        Thread[] threads = new Thread[4];
        CalculateSum[] portion = new CalculateSum[4];
        int[][] subarrays = split(values, 4);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            portion[i] = new CalculateSum(subarrays[i]);
            threads[i] = new Thread(portion[i]);
            threads[i].start();
        }


        for (int i = 0; i < 4; i++) {
            threads[i].join();
        }



        long totalSum = 0;
        for (int i = 0; i < 4; i++) {
            totalSum += portion[i].getSum();
        }

        endTime = System.currentTimeMillis();
        System.out.println("Parallel sum = " + totalSum);
        System.out.println("Parallel time = " + (endTime - startTime));
    }

    static void generateValues(){
        Random rand = new Random();
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextInt(9) + 1;
        }
    }

    private static int[][] split(int[] arr, int parts) {
        int[][] subarrays = new int[parts][];

        int portionSize = arr.length / parts;
        int remain = arr.length % parts;

        int edge = 0;
        for (int i = 0; i < parts; i++) {
            int size = portionSize + (remain-- > 0 ? 1 : 0);
            subarrays[i] = Arrays.copyOfRange(arr, edge, edge + size);
            edge += size;
        }

        return subarrays;
    }
}

class CalculateSum implements Runnable {
    private int[] arr;
    private long sum;

    public CalculateSum(int[] arr) {
        this.arr = arr;
        this.sum = 0;
    }

    public long getSum() {
        return sum;
    }

    @Override
    public void run() {
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        System.out.println(Thread.currentThread().getName() + " calculated sum: " + sum);
    }
}
