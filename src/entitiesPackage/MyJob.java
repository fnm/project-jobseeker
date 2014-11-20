package entitiesPackage;

public class MyJob extends SingleNotificationEntity {

	private Job job;
	private Assignment ass;

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Assignment getAss() {
		return ass;
	}

	public void setAss(Assignment ass) {
		this.ass = ass;
	}

}
