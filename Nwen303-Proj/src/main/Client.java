/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public interface Client {

	public void requireService();
	
	public boolean findProvider(Job temp);
	
	public void addServices(Job temp);
	
	public boolean assesProvider(Job temp);
}
