package utils;

public class Constants {
	
	//Regular expression for all basic SQL queries
	public static String REGEX ="(SELECT\\s[\\w\\*\\)\\(\\,\\s]+\\sFROM\\s[\\w]+)|(UPDATE\\s[\\w]+\\sSET\\s[\\w\\,\\'\\=]+)|(INSERT\\sINTO\\s[\\d\\w]+[\\s\\w\\d\\)\\(\\,]*\\sVALUES\\s\\([\\d\\w\\'\\,\\)]+)|(DELETE\\sFROM\\s[\\d\\w\\'\\=]+)";
	public static String QUERY="SELECT * from table";
	public static int ONE = 1;
	public static int ZERO = 0;
}
