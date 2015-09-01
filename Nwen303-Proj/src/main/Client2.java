/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public interface Client2 {

	public boolean requireService() throws InterruptedException;
	
	public int findProvider(Job2 temp);
	
	public void addServices(Job2 temp) throws InterruptedException;
	
	public int assesProvider(Job2 List,Job2 Create);
	
	public boolean getProvider(int worker) throws InterruptedException;
}
