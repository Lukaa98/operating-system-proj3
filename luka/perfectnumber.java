import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
* PerfectNumber class implements runnable
*/
public class PerfectNumber implements Runnable {

private static int sumOfFactorials = 1, index;
private static int[] factorials;
private static int number;
private int[] imputNumbers;
private static int partitionNo = 0;
private static ArrayList<Integer> factors = new ArrayList<>();

  public PerfectNumber(int[] imputNumbers) {
   this.imputNumbers = imputNumbers;
  }

  public static void main(String[] args) {
  int p;

  System.out.println("Enter your number");

  Scanner in = new Scanner(System.in);
  number = in.nextInt();
  System.out.println("Enter the number of partitions ");
  p = in.nextInt();
  factors.add(1);
  int result = isPerfectNumber(p);
  // sort the factors in ascending oorder (optional)
  Collections.sort(factors);
  if (result == 0) {
    System.out.print(number + " is a PerfectNumber and its factors are ");
      for(int i = 0; i < factors.size(); i++)
       {
          if(i == factors.size() - 1)
          System.out.println(factors.get(i));
           else
            System.out.print(factors.get(i) + ", ");
            }
          }
       if (result != 0) {
     System.out.println("it is not a PerfectNumber (1)");
  }
}

/*
* method that creates p threads and calculates if perfect number or not
*/
public static int isPerfectNumber(int p) {
  if (number > 0 && p < number) {
  factorials = new int[number];
  int noInEachPartition = number / p;
  int remain = number % p;
  if (partitionNo == p) {
  noInEachPartition = noInEachPartition + remain;
 }
 int[] imputNumbers = new int[noInEachPartition];
 int i = 1;
 while (i < number) {
 for (int j = 0; j < noInEachPartition && i < number; j++) {
 imputNumbers[j] = i;
 //System.out.print("p"+i);
 i++;
}
partitionNo++;

Thread object = new Thread(new PerfectNumber(imputNumbers));
object.start();
try {
 object.join();
   } catch (InterruptedException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
if (sumOfFactorials == number) {
return 0;
  }
    else {
    return 1;
    }

  }
  else {
  return -1;
  }

 }

@Override
public void run() {
// TODO Auto-generated method stub
for (int i = imputNumbers[imputNumbers.length - 1]; i > imputNumbers[0]; i--) {

/* sumOfFactorials of it's factors */
if (number % i == 0) {
    sumOfFactorials = sumOfFactorials + i;
    factors.add(i);
    factorials[index] = i;
    index = index + 1;
     }
    }
   }
 }
