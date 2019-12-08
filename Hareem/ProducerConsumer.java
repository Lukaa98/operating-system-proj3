import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Scanner;

/* import java.util.Random; */

public class ProducerConsumer {
    static final int BUFFER_SIZE = 100;
    static int NUM_CONSUMED;
    static Consumer consumer = new Consumer();
    static Producer producer = new Producer();
    static Thread p = new Thread(producer);
    static Thread c = new Thread(consumer);
    static ProducerConsumerMonitor monitor = new ProducerConsumerMonitor();

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
    	System.out.println("Enter a number to find if it is perfect : ");
        NUM_CONSUMED = kb.nextInt();
        kb.close();
        System.out.print("The factors of "+NUM_CONSUMED+" are: ");
        p.start();
        c.start();
        try {
            c.join();
            producer.interrupt();
            p.interrupt();
            System.out.println("and the sum is "+consumer.getSum());
            if(consumer.getSum()==NUM_CONSUMED) {
            	System.out.println("0");
            }
            else {
            	System.out.println("1");
            }
        } catch (InterruptedException e) {
            System.err.println("-1");
        }
    }

    static class Producer implements Runnable {
        private AtomicBoolean running = new AtomicBoolean(false);
        private int number;

        public Producer() {
            number = 0;
        }

        @Override
        public void run() {
            running.set(true);
            while (running.get()) {
                int item = produceItem();
                monitor.insert(item);
            }
        }

        private int produceItem() {
        	int num;
        	number ++;
        	if(number==0) {
        		return 0;
        	}
        	if(NUM_CONSUMED==number)
        		return 0;
        	if(NUM_CONSUMED%number==0) {
        		num = number;
        		System.out.print(number+" ");
        	}
        	else 
        		num = 0;
        		
            return num;
        }


        public void interrupt() {
            running.set(false);
        }
    }


    static class Consumer implements Runnable {
        private int sum; 

        public Consumer() {
            sum = 0;
        }

        @Override
        public void run() {
            for (int i=0; i<NUM_CONSUMED; i++) {
                int item = monitor.remove();
                consumeItem(item);
            }
        }

        private void consumeItem(int item) {
            sum += item;
        }


        public int getSum() {
            return sum;
        }

    }

    static class ProducerConsumerMonitor {
        private int buffer[] = new int[BUFFER_SIZE];
        private int count = 0, out = 0, in = 0;

        public synchronized void insert(int val) {
            if (count == BUFFER_SIZE) { // buffer is full, sleep while waiting
                goToSleep();
            }
            buffer[in] = val;    // insert the produced item to buffer
            in = (in + 1) % BUFFER_SIZE; // advance to next available slot
            count ++;     // count the number of items in buffer
            if (count > 0) {      
                // buffer is not empty any more, wake up consumer to consume
                notify();
            }
        }

        public synchronized int remove() {
            int val;

            if (count == 0) {
                goToSleep();
            }
            val = buffer[out];
            out = (out + 1) % BUFFER_SIZE;
            count --;
            if (count < BUFFER_SIZE) { 
                // buffer is not full any more, wake up producer to produce
                notify();
            }
            return val;
        }

        private void goToSleep() {
            try {
                wait(); 
            } catch (InterruptedException e) {
            }
        }
    }
}