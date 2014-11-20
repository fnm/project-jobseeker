package entitiesPackage;

public class NewsFeedEntity implements Entity {

	Long userId;
	Long currentId;
	String dir;// back/forward
	Long newestPostId;
	Long newsFeedLength;

	public Long getNewsFeedLength() {
		return newsFeedLength;
	}

	public void setNewsFeedLength(Long newsFeedLength) {
		this.newsFeedLength = newsFeedLength;
	}

	public Long getNewestPostId() {
		return newestPostId;
	}

	public void setNewestPostId(Long newestPostId) {
		this.newestPostId = newestPostId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Long currentId) {
		this.currentId = currentId;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
