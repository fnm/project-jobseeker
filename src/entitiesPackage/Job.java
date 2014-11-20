package entitiesPackage;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Job extends SingleNotificationEntity {

	private Long jobId;
	private Long postId;
	private Long submitterUserId;
	private String jobTitle;
	private String companyName;
	private String address;
	private String contact;
	private String description;
	private Boolean isPrivate;
	private Boolean isActive;
	private ArrayList tags;
	private Timestamp publishDate;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public ArrayList getTags() {
		return tags;
	}

	public void setTags(ArrayList tags) {
		this.tags = tags;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getSubmitterUserId() {
		return submitterUserId;
	}

	public void setSubmitterUserId(Long submitterUserId) {
		this.submitterUserId = submitterUserId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

}
