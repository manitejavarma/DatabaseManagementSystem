package utils;

public class Constants {
	
	//Regular expression for all basic SQL queries
	public static String REGEX =  "(create\\s+database\\s+\\w+)|(create\\s+table\\s+\\w+\\s*\\((?:[^()]+|\\([^()]+\\))*\\))|(SELECT\\s[\\w\\*\\)\\(\\,\\s]+\\sFROM\\s[\\w]+)|(UPDATE\\s[\\w]+\\sSET\\s[\\w\\,\\'\\=]+)|(INSERT\\sINTO\\s[\\d\\w]+[\\s\\w\\d\\)\\(\\,]*\\sVALUES\\s\\([\\d\\w\\'\\,\\)]+)|(DELETE\\sFROM\\s[\\d\\w\\'\\=]+)";
	public static String QUERY="create database db;";
	public static String SELECT_REGEX = "^SELECT\\s([\\w\\*\\)\\(\\,\\s]+)\\sFROM\\s([\\w]+)\\sWHERE\\s(\\w+)\\s*\\=\\s*([\"\\w]+);$";
	public static String CREATE_DB_REGEX = "^create\\s+database\\s+(\\w+);$";
	
}
