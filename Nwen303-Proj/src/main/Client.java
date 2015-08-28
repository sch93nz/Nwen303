/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public interface Client {

	public boolean requireService() throws InterruptedException;
	
	public boolean findProvider(Job temp);
	
	public void addServices(Job temp) throws InterruptedException;
	
	public boolean assesProvider(Job List,Job Create);
	
	public boolean getProvider(Job temp) throws InterruptedException;
}
