package mySqlPackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
	
	//static String un="jobseeker",pass="jobseeker",url="jdbc:mysql://jobseeker.ddns.net:3306/jobSeeker?user=jobseeker&password=jobseeker";//jobSeeker is the name of the database
	static String un="jobSeeker",pass="Ts0f3n18C0urs3Cl13ntS3rv3rW3bAppl1cat10n",url="jdbc:mysql://ec2-54-69-86-150.us-west-2.compute.amazonaws.com:3306/jobSeeker?user=jobSeeker&password=Ts0f3n18C0urs3Cl13ntS3rv3rW3bAppl1cat10n";//jobSeeker is the name of the databas
	//static String un="root",pass="",url="jdbc:mysql://localhost:3306/jobSeeker";
	public static Connection setConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url,un,pass);
	        return conn;
		}
	
		catch (SQLException e) 
 	    {
		e.printStackTrace();
        System.out.println("SQLException: " + e.getMessage());
        return null;
 	    }
	}
}