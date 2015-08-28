/**
 * 
 */
package main;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * @author sch93
 *
 */
public class BillBoard {

	private Job[] Jobs = new Job[100];

	private Client[] servicesRequred = new Client[100];

	private Provider[] offeringServices = new Provider[100];
	
	private Semaphore toPut = new Semaphore(100,true);
	
	private Semaphore toGet = new Semaphore(100,true);

	private class Clients implements Runnable,Client{

		private String name;

		@Override
		public void requireService() {
			Job temp = new Job(name, UUID.randomUUID().toString(),true);
			boolean worker = findProvider(temp);
			if(!worker){
				addServices(temp);
			}
		}

		@Override
		public boolean findProvider(Job temp) {
			for(int i=0;i<100;i++){
				boolean take = assesProvider(Jobs[i],temp);
				if(take){
					return true;
				}
			}
			return false;
		}

		@Override
		public void addServices(Job temp) {
			for(int i=0;i<100;i++){
				
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean assesProvider(Job List,Job Create) {
			if(Create.getClientName()==null){
				String Jname = Create.getProviderName();
				String job = Create.getJob();
				int value = Jname.hashCode()+job.hashCode()+name.hashCode();
				value = value%100;
				return value > 50;
			}
			return false;
		}

		@Override
		public boolean getProvider(Job temp) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private class Providers implements Runnable,Provider{

		private String name;

		@Override
		public void offeringServices() {
			Job temp = new Job(name, UUID.randomUUID().toString(),true);
			boolean worker = findClient(temp);
			if(!worker){
				addOffer(temp);
			}
		}

		@Override
		public boolean findClient(Job temp) {
			for(int i=0;i<100;i++){
				boolean take = assesClient(Jobs[i],temp);
				if(take){
					return true;
				}
			}
			return false;
		}

		@Override
		public void addOffer(Job temp) {
			

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean assesClient(Job List,Job Create) {
			if(Create.getProviderName()==null){
				String Jname = Create.getClientName();
				String job = Create.getJob();
				int value = Jname.hashCode()+job.hashCode()+name.hashCode();
				value = value%100;
				return value > 50;
			} 
			return false;
		}
	}
}
