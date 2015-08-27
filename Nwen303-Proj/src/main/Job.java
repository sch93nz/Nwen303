/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public class Job {

	private String clientName;
	
	private String providerName;
	
	private final String Job;
	
	public Job(String Creater, String Job,boolean client){
		if(client){
			clientName=Creater;
		} else {
			providerName=Creater;
		}
		this.Job=Job;
	}
	
	

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the providerName
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * @return the job
	 */
	public String getJob() {
		return Job;
	}

	
}
