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
	
	public boolean findClient(Job temp);
	
	public void addOffer(Job temp) throws InterruptedException;
	
	public boolean assesClient(Job List,Job Create);

	public boolean getClient(Job temp) throws InterruptedException;
	
}
