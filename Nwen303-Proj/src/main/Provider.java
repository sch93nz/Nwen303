/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public interface Provider {

	public void offeringServices();
	
	public boolean findClient(Job temp);
	
	public void addOffer(Job temp);
	
	public boolean assesClient(Job List,Job Create);
	
}
