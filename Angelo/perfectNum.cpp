#include <iostream>
#include <thread>
#include <new>
#include <sstream>
#include <vector>
#include <mutex>
#include <stdio.h>

using namespace std;
mutex locker; 

//Will print out error if user enters threads greater than UserNum or thread =0
void error_message(char** arr_input)
{
  cout<<"-1 " << endl;
  cout<<" Ex:  ./[objectName] 6 2"<< endl;
  
}

//This function will set the N-input and the number of thread the user wants to use
void establish_input (char ** llist, long long &userNumber, int &num_threads)
{
	istringstream(llist [1]) >> userNumber; //Reads from the string stream at the second element 
  istringstream(llist [2]) >> num_threads;	//Reads from the string stream at the third element 

}

// Passes dynamic array(dynamic_list), userNumber and the NumThreads
void max_gauge (long long *& dynamic_list, long long &userNum, int &num_threads)
{
  
  long long temp = (long long) num_threads; //type-cast the value of num_threads into temp
  long long divide_Num = userNum / temp; 
  int counter = 0; //To use in the dynamic array

  for (int i = num_threads; i >= 0; i--){ //If num_of_threads is greater than 0
    dynamic_list[counter]= temp * divide_Num; 
    counter ++;
  }  
}

// Determines if the number is a perfect number
void threads_job(long long top, long long bottom, long long const &userNum, long long const & total)
{
	locker.lock(); //Mutex lock so that no other threads can execute section of code until released
  long long temp = top; //Assigns temp to the top value
  long long & total_value = const_cast <long long&>(total); //type cast from a constant variable to regular variable 
	long long & hold_userNum = const_cast <long long&>(userNum); //type cast from a constant variable to regular variable 

  //Decrement top, if this if-statement runs
  if(top == hold_userNum) { top = top -1; temp = top; }

  // If userNum is divisible by temp, we add to total_value the value of temp
  if (total_value < hold_userNum){
      for (long long i = top; i > bottom; i--){
        
          if(hold_userNum % temp == 0) total_value += temp;
          else if (total > hold_userNum) break;

          temp --;
    }
  }
  locker.unlock(); //Release the lock, so that particular code can be used again by other threads
}

//Prints 0 is the number is Perfect, 1 if not
void print_perfect(long long tot, long long userNum)
{
  if (tot == userNum) cout << "0 " << endl;
    else cout<< "1" << endl;
}

int main(int argc , char** argv)
{
  long long userInput =0; // Holds the number to be tested if perfect
  long long num1 =0, num2 = 0, total =0 ; 
  int num_of_threads =0; //Holds number of threads user want to use
  int num_factors =0, controlled_var =0;
  long long * max;


	vector<int> factors; //Will be a vector to hold the factors of the user input, excluding the input-number
	
	if(argc != 3) // When running the file in terminal, if there isn't 3 arguments, than show error message.
	{
		
		error_message(argv); //Will prompt the error function
    return 0;
	}
  
  //Will take the users arguments and do work with it
  establish_input(argv, userInput, num_of_threads); 

  // If user enters more threads than the actual number OR 0 threads, error
  if(num_of_threads > userInput || num_of_threads == 0)
	{
    error_message(argv);
		return 0;
	}

  //Create a array thread object; the size is the number of threads user desires to use
  thread *thread_array[num_of_threads];
  long long temp = num_of_threads +1;
  max = new long long [temp]; //Dynamically locate an array to max pointer with size numberofthread+1
  max_gauge(max, userInput, num_of_threads);


  for (int i = 0; i < num_of_threads; i++){
    num1 = max[controlled_var];
    num2 = max[++controlled_var];
    thread_array[i] = new thread(threads_job, num1, num2, ref(userInput), ref(total) );
  }


  for( int i = 0; i <num_of_threads; i++){
    thread_array[i] -> join(); //Waits for the thread to finish executing before proceeding 
  }
	
  print_perfect(total, userInput); //Prints 0 if number if perfect, 1 is not.

  delete [] max; //Deallocate the max array po
	
	return 0;
}
