import java.util.ArrayList;
import java.util.List;

class Data {

	  private int value;
	  
	  // These methods are synchronized which means that only one thread can access 
	  // each method at one point in time
	  public synchronized int getValue() {
	    return value;
	  }

	  public synchronized void setValue(final int value) {
	    this.value = value;
	  }

}

public class RaceExample {

  public static void main(final String[] args) throws Exception {
    for (int run = 0, numberOfThreads = 100; run < 1000; run++) {
      System.out.printf("Run %05d.....", run + 1);
      final Data data = new Data();

      final List<Thread> threads = new ArrayList<>(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++) {
        final Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
            final int value = data.getValue();
            data.setValue(value + 1);
          }
        });
        thread.start();
        threads.add(thread);
      }

      // example of join http://www.javatpoint.com/join()-method
      for (final Thread thread : threads) {
        thread.join();
      }

      if (data.getValue() == numberOfThreads) {
        System.out.println("Passed");
      } else {
        System.out.printf("Failed with value %d instead of %d%n", data.getValue(), numberOfThreads);
        break;
      }
    }
  }
}