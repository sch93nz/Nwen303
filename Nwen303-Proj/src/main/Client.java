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
	
	public int findProvider(Job temp);
	
	public void addServices(Job temp) throws InterruptedException;
	
	public int assesProvider(Job List,Job Create);
	
	public boolean getProvider(int worker) throws InterruptedException;
}
