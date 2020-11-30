package DBMS;

import DBMS.attributetype.AttributeType;
import DBMS.metadata.Metadata;
import DBMS.utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser implements ISqlParser {
	private final static Logger logger = Logger.getLogger(SqlParser.class.getName());

    
	public void validateQuery(String query) throws IOException {
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
		}else if(query.toLowerCase().contains("use")){
			useDatabaseQuery(query);
		}else if(query.toLowerCase().contains("grant")){
			grantQuery(query);
		}else if(query.toLowerCase().contains("commit;")){
			commit();
		}else if(query.toLowerCase().contains("rollback;")){
			rollback();
		}
	}

	private void commit() {
		SemanticController semanticController = new SemanticController();
		semanticController.commit();
	}

	private void rollback() {
		SemanticController semanticController = new SemanticController();
		semanticController.rollback();
	}

	void selectQuery(String query) throws IOException {
		Map<String,String> selectFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.SELECT_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			selectFields.put("selectColumn", matcher.group(1));
			selectFields.put("table", matcher.group(2));
			selectFields.put("whereColumn", matcher.group(3));
			selectFields.put("whereValue", matcher.group(4).replace("\"", "").replace("\'", ""));
			SemanticController semanticController = new SemanticController();
			semanticController.selectTable(selectFields);
		}


		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
		
	}
	
	void createDBQuery(String query) throws IOException {
		Map<String,String> createDBFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.CREATE_DB_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			createDBFields.put("database", matcher.group(1));
			SemanticController semanticController = new SemanticController();
			semanticController.createDatabase(createDBFields.get("database"));
		}



		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
	}
	
	void updateQuery(String query) throws IOException {
		Map<String,String> updateFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.UPDATE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			updateFields.put("table", matcher.group(1));
			updateFields.put("column", matcher.group(2));
			updateFields.put("value", matcher.group(3).replace("\"", "").replace("\'", ""));
			updateFields.put("whereColumn", matcher.group(5));
			updateFields.put("whereValue", matcher.group(6).replace("\"", "").replace("\'", ""));
		}

		SemanticController semanticController = new SemanticController();
		semanticController.updateTable(updateFields);
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
	}
	
	void deleteQuery(String query) throws IOException {
		Map<String,String> deleteFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.DELETE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			deleteFields.put("table", matcher.group(1));
			deleteFields.put("whereColumn", matcher.group(2));
			deleteFields.put("whereValue", matcher.group(3).replace("\"", "").replace("\'", ""));
		}
		SemanticController semanticController = new SemanticController();
		semanticController.deleteTable(deleteFields);

		
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
		
	}
	
	void insertQuery(String query) throws IOException {
		Map<String,String[]> insertFields = new HashMap<String, String[]>();
		Pattern pattern = Pattern.compile(Constants.INSERT_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if(matchFound) {
			String[] table = {matcher.group(1)};
			insertFields.put("table", table);
			insertFields.put("columns", matcher.group(2).split(","));
			String values[]=matcher.group(3).split(",");
			for(int i=0;i<values.length;i++)
			{
				values[i]=values[i].replace("\"", "").replace("\'", "");
			}
			insertFields.put("values", values);
		}
		//call to semantic parser
		//if valid, func call to execute the query, pass the map as input 
		SemanticController semanticController = new SemanticController();
		semanticController.insert(insertFields);
	}

	void createQuery(String query) throws IOException {
		Metadata metadata = new Metadata();
		Map<String,String[]> createTableFields = new HashMap<String, String[]>();
		Map<String,AttributeType> columns = new HashMap<String, AttributeType>();
		Pattern pattern = Pattern.compile(Constants.CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		
		if(matchFound) {
			String fields = matcher.group(2);
			fields=fields.substring(0, fields.length()-1);
			System.out.println("fields "+fields);
		
			String[] column = fields.split(",");
			String[] seperateFields;
			AttributeType attribute;
			for(String i : column) {
				seperateFields =i.split(" ");
				attribute = AttributeType.valueOf(seperateFields[1].toUpperCase());
				columns.put(seperateFields[0], attribute);
			}
			
			String[] primaryKeys = matcher.group(4).split(",");
			List<String> primary = Arrays.asList(primaryKeys);
			metadata.setTableName(matcher.group(1));
			metadata.setColumns(columns);
			metadata.setPrimaryKeys(primary);
		}
		SemanticController semanticController = new SemanticController();
		semanticController.createTable(metadata);

	}
	
	void useDatabaseQuery(String query) {
		Pattern pattern = Pattern.compile(Constants.USE_DATABASE, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if(matchFound) {
			String database = matcher.group(1);
			DBMS.getInstance().setActiveDatabase(database);
		}
	}
	
	void grantQuery(String query) throws IOException {
		Map<String,String> grantFields = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(Constants.GRANT_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if(matchFound) {
			grantFields.put("database", matcher.group(1));
			grantFields.put("user", matcher.group(2).replace("\"", "").replace("\'", ""));
			SemanticController semanticController = new SemanticController();
			semanticController.grantAccess(grantFields.get("database"),grantFields.get("user"));
		}
	}
	
}
