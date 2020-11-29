//https://regexlib.com/REDetails.aspx?regexp_id=945&AspxAutoDetectCookieSupport=1
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import utils.Constants;

public class SqlParser implements ISqlParser {
	private final static Logger logger = Logger.getLogger(SqlParser.class.getName());
    
	public void validateQuery(String query) {
		logger.info("Invoked validateQuery method");
		if(query.toLowerCase().contains("select")){
			selectQuery(query);
		}else if(query.toLowerCase().contains("create database")){
			createDBQuery(query);
		}else if(query.toLowerCase().contains("update")){
			updateQuery(query);
		}else if(query.toLowerCase().contains("delete")){
			deleteQuery(query);
		}else if(query.toLowerCase().contains("create table")){
			createQuery(query);
		}else if(query.toLowerCase().contains("insert")){
			insertQuery(query);
		}
	}
	
	void selectQuery(String query) {
		Map<String,String> selectFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.SELECT_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			selectFields.put("selectColumn", matcher.group(1));
			selectFields.put("table", matcher.group(2));
			selectFields.put("whereColumn", matcher.group(3));
			selectFields.put("whereValue", matcher.group(4));
		}
		
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
		
	}
	
	void createDBQuery(String query) {
		Map<String,String> createDBFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.CREATE_DB_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			createDBFields.put("database", matcher.group(1));
		}
		
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
	}
	
	void updateQuery(String query) {
		Map<String,String> updateFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.UPDATE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			updateFields.put("table", matcher.group(1));
			updateFields.put("column", matcher.group(2));
			updateFields.put("value", matcher.group(3));
		}
		
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
	}
	
	void deleteQuery(String query) {
		Map<String,String> deleteFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.DELETE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			deleteFields.put("table", matcher.group(1));
			deleteFields.put("whereColumn", matcher.group(2));
			deleteFields.put("whereValue", matcher.group(3));
		}
		
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
		
	}
	
	void insertQuery(String query) {
		Map<String,String[]> insertFields = new HashMap<String, String[]>();
		Pattern pattern = Pattern.compile(Constants.INSERT_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if(matchFound) {
			String[] table = {matcher.group(1)};
			insertFields.put("table", table);
			insertFields.put("columns", matcher.group(2).split(","));
			String values[]=matcher.group(3).split(",");
			insertFields.put("values", values);
			
		}
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
				
	}

	void createQuery(String query) {
		
		Map<String,String[]> createTableFields = new HashMap<String, String[]>();
		Pattern pattern = Pattern.compile(Constants.CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		System.out.println("match "+matchFound);
		if(matchFound) {
			String fields = matcher.group(2);
			fields=fields.substring(0, fields.length()-1);
			System.out.println("fields "+fields);
			Pattern subPattern = Pattern.compile(Constants.CREATE_TABLE_SUB_REGEX, Pattern.CASE_INSENSITIVE);
			Matcher subMatcher = subPattern.matcher(fields);
			boolean subMatchFound = subMatcher.find();
			System.out.println("match "+subMatchFound);
			if(subMatchFound) {
				System.out.println("column 2 "+subMatcher.group(2).indexOf(0, 4));
				System.out.println("column 3"+subMatcher.group(3));
				System.out.println("colums 4"+subMatcher.group(2).codePointCount(9, 12));
				System.out.println("colums 5"+subMatcher.group(3));
			}
		}
	}
}
