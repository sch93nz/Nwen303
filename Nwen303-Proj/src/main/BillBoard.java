/**
 * 
 */
package main;

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

	private Client[] servicesRequired = new Client[100];

	private Provider[] offeringServices = new Provider[100];

	private Semaphore toPut = new Semaphore(100,true);

	private Semaphore toGet = new Semaphore(100,true);

	private boolean running = true;
	
	public static void main(String[] args){
		new BillBoard().begin(2,2);
	}

	private void begin(int max,int maxThreadPool) {
		System.out.println(System.currentTimeMillis());
		ExecutorService exector = Executors.newFixedThreadPool(maxThreadPool);
		for(int i=0;i<max;i++){
			servicesRequired[i]=new Clients(UUID.randomUUID().toString());
			offeringServices[i]=new Providers(UUID.randomUUID().toString());
			exector.execute((Runnable) servicesRequired[i]);
			exector.execute((Runnable) offeringServices[i]);
		}
		
		try {
			exector.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());
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
				return temp;
			}
		}
		return null;
	}


	private class Clients implements Runnable,Client{

		public final String name;
		private int Client =0;
		/**
		 * @param name
		 */
		public Clients(String name) {
			super();
			this.name = name;
		}

		@Override
		public boolean requireService() throws InterruptedException {
			Job temp = new Job(name, UUID.randomUUID().toString(),true);
			boolean worker = findProvider(temp);
			if(!worker){
				addServices(temp);
				return false;
			}else{
				return getProvider(temp);
			}
		}

		@Override
		public boolean findProvider(Job temp) {
			System.out.println(toString()+ " finding Provider");
			for(int i=0;i<100;i++){
				boolean take = assesProvider(Jobs[i],temp);
				if(take){
					return true;
				}
			}
			return false;
		}

		@Override
		public void addServices(Job temp) throws InterruptedException {
			toPut.acquire();
			put(temp);
			System.out.println("Add Services");
			System.out.println(toString());
			System.out.println(temp.toString());
			toGet.release();
		}

		@Override
		public void run() {
			for(int i=0;i<10;i++){
				try {
					boolean temp =requireService();
					if(temp){
						Thread.sleep(10000);
					}
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(toString()+" Total jobs "+Client);
		}

		@Override
		public boolean assesProvider(Job List,Job Create) {
			if(List!=null && List.getClientName()==null){
				String Lname = List.getProviderName();
				String job = Create.getJob();
				String Ljob= List.getJob();
				int value = Ljob.hashCode()+Lname.hashCode()+job.hashCode()+name.hashCode();
				//System.out.println(value);
				value = Math.abs(value%100);
				//System.out.println(value);
				return value > 50;
			}
			return false;
		}

		@Override
		public boolean getProvider(Job temp) throws InterruptedException {
			toGet.acquire();
			temp = get(temp.hashCode(),this);
			toPut.release();
			System.out.println("Get Provider");
			System.out.println(toString());
			if(temp !=null){
			System.out.println(temp.toString());
			Client++;
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
	}

	private class Providers implements Runnable,Provider{

		public final String name;
		private int Providers=0;
		/**
		 * @param name
		 */
		public Providers(String name) {
			super();
			this.name = name;
		}

		@Override
		public boolean offeringServices() throws InterruptedException {
			Job temp = new Job(name, UUID.randomUUID().toString(),false);
			boolean worker = findClient(temp);
			if(!worker){
				addOffer(temp);
				return false;
			} else {
				return getClient(temp);
			}
		}

		@Override
		public boolean findClient(Job temp) {
			System.out.println(toString()+ " finding Client");
			for(int i=0;i<100;i++){
				boolean take = assesClient(Jobs[i],temp);
				if(take){
					return true;
				}
			}
			return false;
		}

		@Override
		public void addOffer(Job temp) throws InterruptedException {
			toPut.acquire();
			put(temp);
			System.out.println("Adding Offer");
			System.out.println(toString());
			System.out.println(temp.toString());
			toGet.release();
		}

		@Override
		public void run() {
			for(int i=0;i<10;i++){
				try {
					boolean temp =offeringServices();
					if(temp){
						Thread.sleep(10000);
					}
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(toString() + " Total Jobs "+ Providers);
		}

		@Override
		public boolean assesClient(Job List,Job Create) {
			if(List != null && List.getProviderName()==null){
				String Jname = List.getClientName();
				String job = Create.getJob();
				String LJob = List.getJob();
				int value = LJob.hashCode()+Jname.hashCode()+job.hashCode()+name.hashCode();
				//System.out.println(value);
				value = Math.abs(value%100);
				//System.out.println(value);
				return value > 50;
			} 
			return false;
		}

		@Override
		public boolean getClient(Job temp) throws InterruptedException {
			toGet.acquire();
			temp = get(temp.hashCode(),this);
			toPut.release();
			System.out.println("Getting Client");
			System.out.println(toString());
			if(temp!=null){
			System.out.println(temp.toString());
			Providers++;
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

	}
}
