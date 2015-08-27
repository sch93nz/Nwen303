/**
 * 
 */
package main;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author sch93
 *
 */
public class BillBoard {

	private Job[] Jobs = new Job[100];

	private Client[] servicesRequred = new Client[100];

	private Provider[] offeringServices = new Provider[100];	

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
				boolean take = assesProvider(temp);
				if(take){
					return true;
				}
			}
			return false;
		}

		@Override
		public void addServices(Job temp) {



		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean assesProvider(Job temp) {
			if(temp.getClientName()==null){
				String Jname = temp.getProviderName();
				String job = temp.getJob();
				int value = Jname.hashCode()+job.hashCode()+name.hashCode();
				value = value%100;
				return value > 50;
			}
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
				boolean take = assesClient(temp);
				if(take){
					return true;
				}
			}
			return false;
		}

		@Override
		public void addOffer(Job temp) {
			// TODO Auto-generated method stub

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean assesClient(Job temp) {
			if(temp.getProviderName()==null){
				String Jname = temp.getClientName();
				String job = temp.getJob();
				int value = Jname.hashCode()+job.hashCode()+name.hashCode();
				value = value%100;
				return value > 50;
			} 
			return false;
		}
	}
}
