package mySqlPackage;
import exceptionPackage.IllegalOperation;
import java.util.ArrayList;
import entitiesPackage.InitializeEntity;
import entitiesPackage.Tag;

public class InitializeDatabase extends MySqlConnection {

	public InitializeEntity Initialize(Long userId) throws IllegalOperation{
		
		PostDatabase pdb = new PostDatabase();
		UserDatabase udb = new UserDatabase();
		TagDatabase tdb = new TagDatabase();
		
		ArrayList<Tag> tagsList = tdb.getTagTableFromDataBase();
		
		InitializeEntity IE = new InitializeEntity();
		IE.setTags(tagsList);
		IE.setCurrentCounterforPost(pdb.getNewestIdInTable("posts", "postId"));	
		IE.setNumberOfPosts(pdb.getNumberOfPosts());
		IE.setUsers(udb.getUsersList());
		IE.setUserTypes(udb.getUserTypes());
		
		return IE;
	}
}
