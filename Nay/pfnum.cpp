#include <iostream>
#include <thread>
#include <new>
#include <sstream>
#include <chrono>
#include <vector>
#include <mutex>

using namespace std;
mutex mlock;

void show_error(char** arry_input)
{
	cout << arry_input[0]<< " Number Threads"<< endl;
	cout << "Note: Number of Threads should be less than (N) Number and greater than 0"<< endl; 
}

void assign_input (char** arry, long long &num, int &num_threads)
{
	istringstream(arry[1])>> num;
	istringstream(arry[2])>> num_threads;	
}

void limit_size(long long *& dyna_arry, long long &num, int &num_threads)
{
	long long numth = (long long) num_threads;
	long long divp = num / numth;
	int j = 0;
	for(int i= num_threads; i >= 0; i--)
	{
		dyna_arry[j]= divp*i;
		j++;
	}	
}

void init_threads(long long upper, long long bot, long long const &Number, long long const &sum) 
{
	mlock.lock();
	long long upval = upper;
	long long &total_sum = const_cast<long long&>(sum);
	long long &take_num = const_cast<long long&>(Number);
	if(upper == take_num)
	{	
		upper = upper-1;
		upval = upper;
	}
	if(total_sum < take_num)
	{
		for( long long i = upper; i > bot; i--)
		{
			if(take_num%upval == 0)
			{
				total_sum += upval;		
			}
			if(sum > take_num)
			{
				break;
			}			
			upval--;
		}		
	}
	mlock.unlock();

}

int main(int argc , char** argv)
{
	long long inputNum = 0, a = 0 ,sum = 0 , b = 0;
	int threads = 0 , num_facto = 0 , count = 0;
	long long *limit;
	vector<int> factors;
	
	if(argc != 3) 
	{
		show_error(argv);
		return 0;
	}
	assign_input(argv, inputNum, threads);
	
	if(threads > inputNum || threads == 0)
	{
		show_error(argv);
		return 0;
	}

	thread *th_arry[threads];
	long long temp = threads +1;
	limit = new long long [temp];
	limit_size(limit,inputNum,threads);
	
	for(int i = 0; i < threads; i++)
	{	
		a = limit[count];
		b = limit[++count];
		th_arry[i]= new thread(init_threads, a, b, ref(inputNum), ref(sum));	 
	}
	
	for(int i = 0; i < threads; i++)
	{	
		th_arry[i]->join();
	}
	
	cout << sum << endl;

	if( sum == inputNum)
		cout << "0"<< endl;
	else
		cout << "1"<< endl;

	delete [] limit;
	return 0;
}
