/**
 * 
 */
package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author sch93
 *
 */
public class BillBoard {

	private Job[] Jobs = new Job[100];

	private Clients[] servicesRequired = new Clients[100];

	private Providers[] offeringServices = new Providers[100];

	private Semaphore toPut = new Semaphore(100,true);

	private Semaphore toGet = new Semaphore(100,true);

	private boolean running = true;
	
	public FileWriter write;
	
	public static void main(String[] args){
		new BillBoard().begin(16);
	}

	private void begin(int max) {
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
			exector.awaitTermination(10, TimeUnit.MINUTES);
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

	private synchronized boolean put(Job temp){
		for(int i=0;i<100;i++){
			if(Jobs[i]==null){
				Jobs[i] = temp;
				return true;
			}
		}
		return false;
	}

	private synchronized Job get(int hash,Clients client){
		for(int i=0;i<100;i++){
			if (Jobs[i]!=null && Jobs[i].hashCode()==hash){
				Jobs[i].setClientName(client.name);
				Job temp = Jobs[i];
				Jobs[i]=null;
				for(int k=0;k<100;k++){
					if(offeringServices[k]!=null && offeringServices[k].name.equals(temp.getProviderName())){
						offeringServices[k].current = null;
					}
				}
				return temp;
			}
		}
		return null;
	}

	private synchronized Job get(int hash, Providers provider){
		for(int i=0;i<100;i++){
			if (Jobs[i]!=null && Jobs[i].hashCode()==hash){
				Jobs[i].setProviderName(provider.name);
				Job temp = Jobs[i];
				Jobs[i]=null;
				for(int k=0;k<100;k++){
					if(servicesRequired[k]!=null && servicesRequired[k].name.equals(temp.getClientName())){
						servicesRequired[k].current = null;
					}
				}
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

	private class Clients implements Runnable,Client{

		public Job current;
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
			Job temp = new Job(name, UUID.randomUUID().toString(),true);
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
		public int findProvider(Job temp) {
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
		public void addServices(Job temp) throws InterruptedException {
			toPut.acquire();
			put(temp);
			toGet.release();
			offered++;
			addBuf("Add Services\r\n");
			addBuf(toString()+"\r\n");
			addBuf(temp.toString());
		}

		@Override
		public void run() {
			while(running){
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
		public int assesProvider(Job List,Job Create) {
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
			Job temp = get(worker,this);
			toPut.release();
			
			addBuf("Get Provider\r\n");
			addBuf(toString()+"\r\n");
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

	private class Providers implements Runnable,Provider{

		public Job current;
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
			Job temp = new Job(name, UUID.randomUUID().toString(),false);
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
		public int findClient(Job temp) {
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
		public void addOffer(Job temp) throws InterruptedException {
			toPut.acquire();
			put(temp);
			toGet.release();
			offered++;
			addBuf("Adding Offer\r\n");
			addBuf(toString()+"\r\n");
			addBuf(temp.toString());
		}

		@Override
		public void run() {
			while(running){
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
		public int assesClient(Job List,Job Create) {
			if(List != null && List.getProviderName()==null){
				String Jname = List.getClientName();
				String job = Create.getJob();
				String LJob = List.getJob();
				int value = LJob.hashCode()+Jname.hashCode()+job.hashCode()+name.hashCode();
				//addBuf(value);
				value = Math.abs(value%100);
				//addBuf(value);
				if(value >50)
					return List.hashCode();
			} 
			return -1;
		}

		@Override
		public boolean getClient(int worker) throws InterruptedException {
			toGet.acquire();
			Job temp = get(worker,this);
			toPut.release();
			addBuf("Getting Client\r\n");
			addBuf(toString()+"\r\n");
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
