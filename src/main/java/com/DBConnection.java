package com;

import java.sql.*;
import java.util.*;

public class DBConnection {
	
	private static ResultSet res = null;
	private static PreparedStatement pstmt = null;
	private static Connection conn = null;
	private static Vector<String> result = new Vector<String>();
	
	public static Connection getConn(){
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xe","asdf","1234");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			e.printStackTrace();
		}
		return conn;
	}
	public static ResultSet sendQuery(String query){
		if(conn == null)getConn();
		try {
			pstmt = conn.prepareStatement(query);
			res = pstmt.executeQuery();
//			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			e.printStackTrace();
		}
		return res;
	}
	public static String insertQuery(String query) {
		if(conn == null)getConn();
		System.out.println(query);
		try {
			pstmt = conn.prepareStatement(query);
			res = pstmt.executeQuery();
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			e.printStackTrace();
			return "Error : "+query;
		}
		return "적용되었습니다";
	}
	
	public static String showDB(String messageContent) {
		String getMessage[] = messageContent.split(" ");
		if(getMessage.length != 2) {
			return "테이블명 적어라";
		}
		String table = getMessage[1];
		String query = "select * from "+table;
		ResultSet res = DBConnection.sendQuery(query);
		String result_text = "============================================\n";
		result_text += "All DATA-"+table+"\n\n";
		try {
			while(res.next()) {
				result_text += res.getString(1)+"	"+res.getString(2)+"	"+res.getString(3)+"	"+res.getString(4)+"\n";
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		result_text += "============================================";
		
		return result_text;
	}
	
}	