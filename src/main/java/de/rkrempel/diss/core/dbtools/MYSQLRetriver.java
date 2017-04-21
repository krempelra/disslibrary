package de.rkrempel.diss.core.dbtools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Mysql Tool
 */
public class MYSQLRetriver {
	Connection ds;
	String TargetDB;
	/**
	 * Constructor
	 * @param d Database Connection
	 * @param TargetD Name Target Database
	 */
	
	public MYSQLRetriver(Connection d,String TargetD) {
		ds =d;
		TargetDB=TargetD;

	}
	
	public List<String> getNthfieldsAsString(int n ,String Query) throws SQLException{
		List<String> result = new LinkedList<String>();
		//System.out.println(Query);
		Statement st= null;
		try {
			st = ds.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        ResultSet res = st.executeQuery(Query);
        while(res.next()){
        	String a="";
        	if (res != null){
        		String temp = res.getString(n);
        		if(temp == null)
        			temp = new String("");
        		a = temp.replace("\r", " ").replace("\n", " ").replace("\t", " ").replace(">", "").replace("<", "").replace("\"", "");
        	result.add(new String(a));
        	}
        }
        res.close();
        st.close();
		return result;
		
	}
	
	public List<Long> getNthfieldsAsLong(int n ,String Query) throws SQLException{
		List<Long> result = new LinkedList<Long>();
		Statement st= null;
		try {
			st = ds.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Query);
        ResultSet res = st.executeQuery(Query);
        while(res.next()){
        	Long a = res.getLong(n);
        	result.add(new Long(a));
        }
        res.close();
        st.close();
		return result;
		
	}
	
	public Connection getConnection() {
		return ds;
	}
	
	
	
	
}
