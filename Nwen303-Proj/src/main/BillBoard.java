/**
 * 
 */
package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author sch93
 *
 */
public class BillBoard {

	private Job[] jobs= new Job[100];

	private static final int MAX_JOBS=200;

	private final Semaphore toPut = new Semaphore(MAX_JOBS,true);

	private final Semaphore toGet = new Semaphore(MAX_JOBS,true);

	private boolean running = true;

	private Random rnd = new Random();

	public static void main(String[] args){


		ExecutorService exector = Executors.newFixedThreadPool(2);
		for (int i = 0 ; i<10 ;i++ ){

		}
	}

	public class Client implements Runnable{

		private int numjobs = 0;
		private String ProducerName;
		private Job current;
		private int numJobs;
		private int click =0;

		@Override
		public void run() {
			while(running){
				try {
					if(click %3 == 0) {current = null;}
					consum();
					putJob();
					click++;
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		/**
		 * @throws InterruptedException
		 */
		private void putJob() throws InterruptedException {
			if(current == null || current.getWorker() !=null){
				Job temp = createJob();
				current = temp;
				toPut.acquire();
				for (int i = 0 ; i<100 ;i++ ){
					if(jobs[i]==null){
						jobs[i]=temp;
						break;
					}
				}
				toGet.release();
			}
		}

		private Job createJob() {
			return new Job("job "+numjobs++,ProducerName,
					rnd.nextInt(),UUID.randomUUID().toString());
		}

		/**
		 * @throws InterruptedException
		 */
		private void consum() throws InterruptedException {
			if(current == null){
				for (int i = 0 ; i<100 ;i++ ){
					if(Takejob(jobs[i])){
						toGet.acquire();
						getJob(i);
						toPut.release();

					}
				}
			}
		}

		private void getJob(int i) {
			Job temp = jobs[i];
			temp.setWorker(ProducerName);
			current= temp;
			numJobs++;
		}


		private boolean Takejob(Job job) {
			int number = job.getRating(numJobs,ProducerName);
			if(number ==-1) return false;
			System.out.println(number+rnd.nextInt(20)%100);
			System.out.println(number+rnd.nextInt(20)%100 > 50);
			return	(number+rnd.nextInt(20)%100) > 50;
		}

	}

	public class Providers implements Runnable{

		private int numJobs =0;
		private String ConsumerName;
		private Job takenJob;
		private int click =0;

		@Override
		public void run() {
			while(running){
				try {
					if(click%3 ==0){ takenJob=null;}
					consum();
					putJob();
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/**
		 * @throws InterruptedException
		 */
		private void putJob() throws InterruptedException {
			if(takenJob == null || takenJob.getWorker() !=null){
				Job temp = createJob();
				takenJob= temp;
				toPut.acquire();
				for (int i = 0 ; i<100 ;i++ ){
					if(jobs[i]==null){
						jobs[i]=temp;
						break;
					}
				}
				toGet.release();
			}
		}

		private Job createJob() {
			return new Job("job "+numJobs++,ConsumerName,
					rnd.nextInt(),UUID.randomUUID().toString());
		}


		/**
		 * @throws InterruptedException
		 */
		private void consum() throws InterruptedException {
			for (int i = 0 ; i<100 ;i++ ){
				if(Takejob(jobs[i])){
					toGet.acquire();
					getJob(i);
					toPut.release();

				}
			}
		}

		private void getJob(int i) {
			Job temp = jobs[i];
			jobs[i] = null;
			temp.setWorker(ConsumerName);
			takenJob = temp;
			numJobs++;
		}


		private boolean Takejob(Job job) {
			System.out.println(job.getRating(numJobs,ConsumerName)+rnd.nextInt(20)%100);
			System.out.println((job.getRating(numJobs,ConsumerName)+rnd.nextInt(20)%100) > 50);
			return	(job.getRating(numJobs,ConsumerName)+rnd.nextInt(20)%100) > 50;
		}

	}


}
