//https://regexlib.com/REDetails.aspx?regexp_id=945&AspxAutoDetectCookieSupport=1
import java.util.HashMap;
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
		}

		

	}
	
	void selectQuery(String query) {
		Map<String,String> selectFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.SELECT_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			selectFields.put("selectColumns", matcher.group(1));
			selectFields.put("tableName", matcher.group(2));
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

}
