package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseService {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
	static final String DB_URL = "jdbc:mysql://titan.cmpe.boun.edu.tr:3306/database6";
	static final String USER = "project6";
	static final String PASS = "xXumhNf4";
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	public DatabaseService()
	{
		
	}
	Boolean executeSql(String sql)
	{
		Boolean result = false;
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			result = true;
		}catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
		}catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	   		try{
	   			if(stmt!=null)
	   				stmt.close();
	   		}catch(SQLException se2){
	   		}// nothing we can do
	   		try{
	   			if(conn!=null)
	   				conn.close();
	   		}catch(SQLException se){
	   			se.printStackTrace();
	   		}//end finally try
	    }
		return result;
	}
	
	User findUserByEmail(String mail)
	{
		User user = new User();
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("SELECT * FROM Users WHERE Mail = ? and IsDeleted = 0");
			pstmt.setString(1, mail);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				user.setName(rs.getString("Name"));
				user.setEmail(mail);
				user.setUserId(rs.getInt("UserID"));
				user.setBirthdate(rs.getDate("Birthdate"));
				user.setPhone(rs.getString("Phone"));
				user.setExperiencePoint(rs.getInt("ExperiencePoint"));
				user.setLevel(rs.getInt("Level"));
				user.setIsDeleted(0);
				user.setPicId(rs.getInt("PicID"));
				user.setBio(rs.getString("Bio"));
				user.setPassword(rs.getString("Password"));
				user.setCreatedOn(rs.getDate("CreationDate"));
				user.setUpdatedOn(rs.getDate("LastUpdate"));
			}
			return user;
		}catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
		}catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	   		try{
	   			if(stmt!=null)
	   				stmt.close();
	   		}catch(SQLException se2){
	   		}// nothing we can do
	   		try{
	   			if(conn!=null)
	   				conn.close();
	   		}catch(SQLException se){
	   			se.printStackTrace();
	   		}//end finally try
	    }
		return null;
	}
	
	public void executeStmt(PreparedStatement stmt)
	{
		
	}
}
