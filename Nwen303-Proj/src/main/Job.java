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



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Job == null) ? 0 : Job.hashCode());
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + ((providerName == null) ? 0 : providerName.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (Job == null) {
			if (other.Job != null)
				return false;
		} else if (!Job.equals(other.Job))
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (providerName == null) {
			if (other.providerName != null)
				return false;
		} else if (!providerName.equals(other.providerName))
			return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Job [" + (clientName != null ? "clientName=" + clientName + ", " : "")
				+ (providerName != null ? "providerName=" + providerName + ", " : "")
				+ (Job != null ? "Job=" + Job : "") + "]  at "+System.currentTimeMillis();
	}

	
}
