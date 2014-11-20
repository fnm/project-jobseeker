package entitiesPackage;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Post extends SingleNotificationEntity {
	private Long postId;
	private Long userId;
	private String title;
	private String content;
	private ArrayList tags;
	private Timestamp publishDate;
	private String pictureUrl;
	private String firstName;
	private String lastName;

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList getTags() {
		return tags;
	}

	public void setTags(ArrayList tags) {
		this.tags = tags;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long l) {
		this.userId = l;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

}
