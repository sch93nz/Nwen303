 /**
 * 
 */
package main;

import java.util.Arrays;

/**
 * @author sch93
 *
 */
public class Job2 {

	private String clientName;
	
	private String[] clientsName;
	
	private String providerName;
	
	private final String Job;
	
	private int Max=1,Current=0;
	
	
	public Job2(String Creater, String Job,boolean client){
		if(client){
			clientName=Creater;
		} else {
			providerName=Creater;
			clientsName = new String[1];
		}
		this.Job=Job;
	}
	
	public Job2(String Provider, String job, int number){
		Job = job;
		providerName = Provider;
		clientsName = new String[number];
		Max = number;
		
	}

	
	/**
	 * 
	 * @param The clientName
	 * @return
	 */
	public boolean AddClient(String c){
		if(Current +1 < Max){
			clientsName[Current]=c;
			Current++;
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * is the Client list full 
	 * @return
	 */
	public boolean isFull(){
		return Current == (Max-1);
	}
	
	/**
	 * Is the CLient List Empty
	 * @return
	 */
	public boolean isEmpty(){
		return Current == 0 ;
	}
	
	public boolean isClientInLsit(String n){
		for (int i=0; i< Max; i++){
			if(clientsName[i] != null&& n!= null && clientsName[i].equals(n)){
				return true;
			}
		}
		return false;
	}
	
	public int ClientListHash(){
		int hash=0;
		for (int i=0; i< Max; i++){
			hash += clientsName[i].hashCode();
		}
		return hash;
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
		result = prime * result + Current;
		result = prime * result + ((Job == null) ? 0 : Job.hashCode());
		result = prime * result + Max;
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + Arrays.hashCode(clientsName);
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
		Job2 other = (Job2) obj;
		if (Current != other.Current)
			return false;
		if (Job == null) {
			if (other.Job != null)
				return false;
		} else if (!Job.equals(other.Job))
			return false;
		if (Max != other.Max)
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (!Arrays.equals(clientsName, other.clientsName))
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
		return "Job2 [" + (clientName != null ? "clientName=" + clientName + ", " : "")
				+ (clientsName != null ? "clientsName=" + Arrays.toString(clientsName) + ", " : "")
				+ (providerName != null ? "providerName=" + providerName + ", " : "")
				+ (Job != null ? "Job=" + Job + ", " : "") + "Max=" + Max + ", Current=" + Current + "] at "+System.currentTimeMillis();
	}



	

	
}
