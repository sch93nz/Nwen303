/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public interface Provider {

	public boolean offeringServices() throws InterruptedException;
	
	public int findClient(Job temp);
	
	public void addOffer(Job temp) throws InterruptedException;
	
	public int assesClient(Job List,Job Create);

	public boolean getClient(int worker) throws InterruptedException;
	
}
