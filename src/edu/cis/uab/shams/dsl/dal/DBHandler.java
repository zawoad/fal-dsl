package edu.cis.uab.shams.dsl.dal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBHandler {
	private Connection db;
	
	public DBHandler()
	{
		db = DBConnection.DB("dsl", "postgres", "123456");
	}
	
	public void insertData(List<String> queryList) throws Exception
	{
		
		Statement sql = db.createStatement();
		for(String query: queryList)
		{
			sql.executeUpdate(query);
		}
		sql.close();
		
	}
	
	public void insertData(String query) throws Exception
	{
		Statement sql = db.createStatement();
		sql.executeUpdate(query);
		sql.close();
	}
	
	public String getSingleData(String query, String field) throws Exception
	{
		Statement sql = db.createStatement();
		ResultSet rs = sql.executeQuery(query);
		if(rs!=null && rs.next()){
           return rs.getString(field);
        }
		return "";
	}
}
