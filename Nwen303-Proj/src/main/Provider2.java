/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public interface Provider2 {

	public boolean offeringServices() throws InterruptedException;
	
	public int findClient(Job2 temp);
	
	public void addOffer(Job2 temp) throws InterruptedException;
	
	public int assesClient(Job2 List,Job2 Create);

	public boolean getClient(int worker) throws InterruptedException;
	
}
