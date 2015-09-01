/**
 * 
 */
package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author sch93
 *
 */
public class BillBoard2 {

	private Job2[] Jobs = new Job2[100];

	private Clients[] servicesRequired = new Clients[100];

	private Providers[] offeringServices = new Providers[100];

	private Semaphore toPut = new Semaphore(100,true);

	private Semaphore toGet = new Semaphore(100,true);

	private boolean running = true;

	private FileWriter write;

	private Random RND;

	public static void main(String[] args){
		int num = Integer.parseInt(args[0]);
		int time = Integer.parseInt(args[1]);
		new BillBoard2().begin(num,time);
	}

	private void begin(int max,int time) {
		RND = new Random();
		try {
			File temp = new File("runAt"+System.currentTimeMillis()+".txt");

			write = new FileWriter(temp);
			write.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		addBuf(""+System.currentTimeMillis()+"\r\n");
		ExecutorService exector = Executors.newFixedThreadPool(max*2);
		for(int i=0;i<max;i++){
			servicesRequired[i]=new Clients(UUID.randomUUID().toString());
			offeringServices[i]=new Providers(UUID.randomUUID().toString());
			exector.execute(servicesRequired[i]);
			exector.execute(offeringServices[i]);
		}

		try {
			exector.awaitTermination(time, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		running = false;
		for(int i =0 ;i<max;i++){
			addBuf(servicesRequired[i].endStats());
			addBuf(offeringServices[i].endStates());
		}

		addBuf(""+System.currentTimeMillis()+"\r\n");

		try {
			write.flush();
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done!!!");
		System.exit(0);
	}

	private synchronized boolean put(Job2 temp){
		for(int i=0;i<100;i++){
			if(Jobs[i]==null){
				Jobs[i] = temp;
				return true;
			}
		}
		return false;
	}

	private synchronized Job2 get(int hash,Clients client){
		for(int i=0;i<100;i++){
			if (Jobs[i]!=null && Jobs[i].hashCode()==hash&& Jobs[i].isClientInLsit(client.name)){
				Jobs[i].AddClient(client.name);
				Job2 temp = Jobs[i];
				if(Jobs[i].isFull()){
					for(int k=0;k<100;k++){
						if(offeringServices[k]!=null && offeringServices[k].name.equals(temp.getProviderName())){
							offeringServices[k].current = null;	
						}
						if(servicesRequired[k]!=null && Jobs[i].isClientInLsit(servicesRequired[k].name)){
							servicesRequired[k].current=null;
						}
					}
					Jobs[i]=null;
				}
				return temp;
			}
		}
		return null;
	}

	private synchronized Job2 get(int hash, Providers provider){
		for(int i=0;i<100;i++){
			if (Jobs[i]!=null && Jobs[i].hashCode()==hash){
				Jobs[i].setProviderName(provider.name);
				Job2 temp = Jobs[i];
				for(int k=0;k<100;k++){
					if(servicesRequired[k]!=null && servicesRequired[k].name.equals(temp.getClientName())){
						servicesRequired[k].current = null;
					}
					if(offeringServices[k]!=null && offeringServices[k].name.equals(temp.getProviderName())){
						offeringServices[k].current = null;	
					}
				}
				Jobs[i]=null;
				return temp;
			}
		}
		return null;
	}


	private synchronized void addBuf(String temp){
		try {
			write.append(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class Clients implements Runnable,Client2{

		public Job2 current;
		public final String name;
		private int gotten =0;
		private int offered = 0;

		/**
		 * @param name
		 */
		public Clients(String name) {
			super();
			this.name = name;
		}

		@Override
		public boolean requireService() throws InterruptedException {
			if(current == null){
				Job2 temp = new Job2(name, UUID.randomUUID().toString(),true);
				current = temp;
				int worker = findProvider(temp);
				if(worker==-1){
					addServices(temp);
					return false;
				}else{
					return getProvider(worker);
				}
			}else{
				int worker = findProvider(current);
				if(worker!=-1)return getProvider(worker);
			}
			return false;
		}

		@Override
		public int findProvider(Job2 temp) {
			addBuf(toString()+"\r\n"+ "finding Provider\r\n");
			for(int i=0;i<100;i++){
				int take = assesProvider(Jobs[i],temp);
				if(take!= -1){
					return take;
				}
			}
			return -1;
		}

		@Override
		public void addServices(Job2 temp) throws InterruptedException {
			toPut.acquire();
			put(temp);
			toGet.release();
			offered++;
			addBuf("Add Services\r\n"+toString()+"\r\n"+temp.toString());
		}

		@Override
		public void run() {
			while(running){
				if(current != null && current.isFull()){
					current = null;
				}

				try {
					boolean temp =requireService();
					if(temp){
						Thread.sleep(1000);
					}
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		@Override
		public int assesProvider(Job2 List,Job2 Create) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(List!=null && List.getClientName()==null){
				
				String Lname = List.getProviderName();
				String job = Create.getJob();
				String Ljob= List.getJob();
				int value = Ljob.hashCode()+Lname.hashCode()+job.hashCode()+name.hashCode();
				//addBuf(value);
				value = Math.abs(value%100);
				//addBuf(value);
				if(value >50){
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return List.hashCode();
				}
			}
			return -1;
		}

		@Override
		public boolean getProvider(int worker) throws InterruptedException {
			toGet.acquire();
			Job2 temp = get(worker,this);
			toPut.release();

			addBuf("Get Provider\r\n"+toString()+"\r\n");
			if(temp !=null){
				addBuf(temp.toString());
				gotten++;
			}
			return temp!=null;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Clients [" + (name != null ? "name=" + name : "") + "] at "+System.currentTimeMillis();
		}

		public String endStats(){
			return (toString()+" Total jobs "+gotten+" Total offered "+ offered+"\r\n");
		}
	}

	private class Providers implements Runnable,Provider2{

		public Job2 current;
		public final String name;
		private int gotten=0;
		private int offered =0;
		/**
		 * @param name
		 */
		public Providers(String name) {
			super();
			this.name = name;
		}

		@Override
		public boolean offeringServices() throws InterruptedException {
			if(current ==null){
				Job2 temp = new Job2(name, UUID.randomUUID().toString(),RND.nextInt(9)+1);
				current = temp;
				int worker = findClient(temp);
				if(worker==-1){
					addOffer(temp);
					return false;
				} else {
					return getClient(worker);
				}
			}else{
				int worker = findClient(current);
				if(worker!=-1)
					return getClient(worker);
			}
			return false;
		}

		@Override
		public int findClient(Job2 temp) {
			addBuf(toString()+"\r\n"+ "finding Client\r\n");
			for(int i=0;i<100;i++){
				int take = assesClient(Jobs[i],temp);
				if(take!=-1){
					return take;
				}
			}
			return -1;
		}

		@Override
		public void addOffer(Job2 temp) throws InterruptedException {
			toPut.acquire();
			put(temp);
			toGet.release();
			offered++;
			addBuf("Adding Offer\r\n"+toString()+"\r\n"+temp.toString());
		}

		@Override
		public void run() {
			while(running){
				if(current != null && current.isFull()){
					current = null;
				}
				try {
					boolean temp =offeringServices();
					if(temp){
						Thread.sleep(500);
					}
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		@Override
		public int assesClient(Job2 List,Job2 Create) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(List != null && List.getProviderName()==null){
				String Jname = List.getClientName();
				String job = Create.getJob();
				String LJob = List.getJob();
				int value = LJob.hashCode()+Jname.hashCode()+job.hashCode()+name.hashCode();
				//addBuf(value);
				value = Math.abs(value%100);
				//addBuf(value);
				if(value >50){
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return List.hashCode();
				}
			} 
			return -1;
		}

		@Override
		public boolean getClient(int worker) throws InterruptedException {
			toGet.acquire();
			Job2 temp = get(worker,this);
			toPut.release();
			addBuf("Getting Client\r\n" + toString()+"\r\n");
			if(temp!=null){
				addBuf(temp.toString());
				gotten++;
			}
			return temp!=null;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Providers [" + (name != null ? "name=" + name : "") + "] at "+System.currentTimeMillis();
		}


		public String endStates (){
			return (toString() + " Total Jobs "+ gotten + " Total Offered " +offered+"\r\n");
		}
	}
}
