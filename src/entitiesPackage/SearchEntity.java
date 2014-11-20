package entitiesPackage;

import java.util.ArrayList;

public class SearchEntity implements Entity {

	private ArrayList tagsIds;
	private Long userId;
	private Long numberOfItems;
	private Long offset;
	private boolean forward;

	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public Long getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(Long numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public ArrayList getTagsIds() {
		return tagsIds;
	}

	public void setTagsIds(ArrayList tagsIds) {
		this.tagsIds = tagsIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
