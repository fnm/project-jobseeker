package entitiesPackage;

import java.util.ArrayList;

public class SuggestJobEntity implements Entity {

	private Long userId;
	private Long jobId;
	private ArrayList jobseekersList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public ArrayList getJobseekersList() {
		return jobseekersList;
	}

	public void setJobseekersList(ArrayList jobseekersList) {
		this.jobseekersList = jobseekersList;
	}

}
