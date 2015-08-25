/**
 * 
 */
package main;

/**
 * @author sch93
 *
 */
public class Job {

	private String name;
	
	private String Creater;
	
	private String Worker;
	
	private Integer randomNumber;
	
	private String RandomString;
	
	
	
/**
 * 	
 * @param name
 * @param creater
 * @param randomPrime
 * @param randomString
 */
	public Job(String name, String creater, Integer randomNumber, String randomString) {
		super();
		this.name = name;
		Creater = creater;
		this.randomNumber = randomNumber;
		RandomString = randomString;
	}

	/**
	 * @return the worker
	 */
	public String getWorker() {
		return Worker;
	}

	/**
	 * @param worker the worker to set
	 */
	public void setWorker(String worker) {
		Worker = worker;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the creater
	 */
	public String getCreater() {
		return Creater;
	}

	/**
	 * @return the randomPrime
	 */
	public Integer getRandomPrime() {
		return randomNumber;
	}

	/**
	 * @return the randomString
	 */
	public String getRandomString() {
		return RandomString;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Job [" + (name != null ? "name=" + name + ", " : "")
				+ (Creater != null ? "Creater=" + Creater + ", " : "")
				+ (Worker != null ? "Worker=" + Worker + ", " : "")
				+ (randomNumber != null ? "RandomPrime=" + randomNumber + ", " : "")
				+ (RandomString != null ? "RandomString=" + RandomString : "") + "]";
	}
	
	
	public int getRating(int numJobs, String consumerName){
		return (name.hashCode()+Creater.hashCode()+randomNumber+RandomString.hashCode())-(numJobs+consumerName.hashCode())*2;
		
	}
	
}
