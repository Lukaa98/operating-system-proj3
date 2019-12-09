import java.util.Scanner;
/*
* PerfectNumber class implements runnable
*/
public class PerfectNumber implements Runnable{
   private static int sum=1, index;
   private static int[] factorials ;
   private static int num;
   private int [] imputNums;
   private static int partitionNo=0;

   public PerfectNumber(int [] imputNums){
       this.imputNums = imputNums;
   }

   public static void main(String[] args) {
       int p;

       System.out.println("Enter a number");

       Scanner in = new Scanner(System.in);
       num = in.nextInt();
       System.out.println("Enter number of partitions ");
       p = in.nextInt();
       int result = isPerfectNumber(p);
       if(result==0){
           System.out.println("Its a perfect number");
       }else{
           System.out.println("Its not a perfect number");
       }

   }
   /*
   * method that creates p threads and calculates if perfect number or not
   */
   public static int isPerfectNumber( int p){
       if (num > 0 && p<num) {
           factorials = new int[num];
           int noInEachPartition = num / p;
           int remain = num%p;
           if(partitionNo == p)
               noInEachPartition = noInEachPartition + remain;
           int[] imputNums = new int[noInEachPartition];
           int i = 1;
           while(i<num){
               for (int j = 0; j < noInEachPartition && i<num ; j++) {
                   imputNums[j] = i;
                   //System.out.print("p"+i);
                   i++;
               }
               partitionNo++;

               Thread object = new Thread(new PerfectNumber(imputNums));
   object.start();
   try {
                   object.join();
               } catch (InterruptedException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
           }
           if(sum==num ){
               return 0;
           }else{
               return 1;
           }

       }else return -1;


   }

   @Override
   public void run() {
       // TODO Auto-generated method stub
       for (int i = imputNums[imputNums.length-1]; i >imputNums[0] ; i--) {

           /* sum of it's factors */
               if ( num % i == 0) {
                   sum = sum + i;
                   factorials[index]=i;
                   index = index+1;
                   //System.out.println(num);
                   //System.out.println(i);
               }
           }





   }

}
