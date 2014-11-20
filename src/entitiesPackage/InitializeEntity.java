package entitiesPackage;

import java.util.ArrayList;

public class InitializeEntity implements Entity {

	Long currentCounterforPost;
	ArrayList tags;
	Long NumberOfPosts;
	ArrayList userTypes;
	ArrayList users;

	public Long getCurrentCounterforPost() {
		return currentCounterforPost;
	}

	public void setCurrentCounterforPost(Long currentCounterforPost) {
		this.currentCounterforPost = currentCounterforPost;
	}

	public ArrayList getTags() {
		return tags;
	}

	public void setTags(ArrayList tags) {
		this.tags = tags;
	}

	public Long getNumberOfPosts() {
		return NumberOfPosts;
	}

	public void setNumberOfPosts(Long numberOfPosts) {
		NumberOfPosts = numberOfPosts;
	}

	public ArrayList getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(ArrayList userTypes) {
		this.userTypes = userTypes;
	}

	public ArrayList getUsers() {
		return users;
	}

	public void setUsers(ArrayList users) {
		this.users = users;
	}

}
