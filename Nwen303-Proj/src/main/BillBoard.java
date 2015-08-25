/**
 * 
 */
package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * @author sch93
 *
 */
public class BillBoard {

	private ArrayDeque<Job> jobs= new ArrayDeque<Job>();
	
	private static final int MAX_JOBS=200;
	
	private final Semaphore toPut = new Semaphore(MAX_JOBS,true);
	
	private final Semaphore toGet = new Semaphore(MAX_JOBS,true);
	
	private boolean running = true;
	
	private Random rnd = new Random();
	
	
	
	public class Producer implements Runnable{

		private int numjobs = 0;
		private String ProducerName;
		
		@Override
		public void run() {
			while(running){
				Job temp = createJob();
				try {
					toPut.acquire();
					jobs.add(temp);
					toGet.release();
					wait(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		private Job createJob() {
			return new Job("job "+numjobs++,ProducerName,
					rnd.nextInt(),UUID.randomUUID().toString());
		}
		
	}
	
	public class Consumer implements Runnable{

		private int numJobs =0;
		private String ConsumerName;
		private ArrayList<Job> takenJobs = new ArrayList<Job>();
		
		@Override
		public void run() {
			while(running){
					if(Takejob(jobs.peekFirst())){
						try {
						toGet.acquire();
						getJob();
						toPut.release();
						wait(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			}
		}
				
		private void getJob() {
			Job temp = jobs.pop();
			temp.setWorker(ConsumerName);
			takenJobs.add(temp);
			numJobs++;
		}

		
		private boolean Takejob(Job job) {
		return	(job.getRating(numJobs,ConsumerName)%100) > 50;
		}
		
	}
	
	
}
