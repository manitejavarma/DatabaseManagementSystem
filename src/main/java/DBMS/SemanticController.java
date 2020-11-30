package DBMS;

import DBMS.metadata.Metadata;
import DBMS.metadata.MetadataManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class SemanticController {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SemanticController.class);
    SQLToCSV sqlToCSV;
    MetadataManager metadataManager;
    Transaction transaction;

    SemanticController(){
        this.sqlToCSV = new SQLToCSV();
        metadataManager = new MetadataManager();
        transaction = new Transaction();
    }

    public void insert(Map<String, String[]> insertFields) throws IOException {
        String tableName = insertFields.get("table")[0];
        String[] columns = insertFields.get("columns");
        String[] values = insertFields.get("values");

        //semantic check
        if(!metadataManager.tableExists(DBMS.getInstance().getActiveDatabase(),tableName)){
            System.out.println("table " + tableName +"is not present in the table. Please retry");
            return;
        }
        if(!columns[0].isEmpty()){
            for(String column: columns){
                if(!metadataManager.columnExists(DBMS.getInstance().getActiveDatabase(),tableName,column)){
                    System.out.println("column " + column +"is not present in the table. Please retry");
                    return;
                }
            }
        }

        if(transaction.checkIfAnyTransactionUsingSametable(tableName)){
            System.out.println("table " + tableName + " is currently locked by other user. please try again later" );
            return;
        }


        transaction.copyTable(DBMS.getInstance().getActiveDatabase(),tableName);

        HashMap<String,String> columnsAndValues = new HashMap<>();
        if(columns[0].isEmpty()){
            //Setting columns and values to insert into CSV
            List<String> allTableColumns = this.sqlToCSV.getHeaders(DBMS.getInstance().getActiveDatabase(),tableName);

            for(int i =0;i<allTableColumns.size();i++){
                columnsAndValues.put(allTableColumns.get(i),values[i]);
            }
        }else{

            //Setting columns and values to insert into CSV
            for(int i =0;i<columns.length;i++){
                columnsAndValues.put(columns[i],values[i]);
            }
        }
        LocalDateTime startOfQuery  = java.time.LocalDateTime.now();

        sqlToCSV.insertQuery(DBMS.getInstance().getActiveDatabase(), transaction.getCopyTableName(DBMS.getInstance().getActiveDatabase(),tableName),columnsAndValues);

        LocalDateTime endOfQuery  = java.time.LocalDateTime.now();
        //Transaction Log
        transaction.insertTransactionLog(tableName,"insert",startOfQuery,endOfQuery);

        DBMS.getInstance().getTables().add(tableName);
    }

    public void createTable(Metadata metadata) throws IOException {

        String tableName = metadata.getTableName();

        //semantic check
        if(metadataManager.tableExists(DBMS.getInstance().getActiveDatabase(),tableName)){
            System.out.println("table " + tableName +" already exists in the database. Please retry");
            return;
        }

        metadataManager.createTableMetadata(DBMS.getInstance().getActiveDatabase(), tableName,metadata);
        ArrayList<String> listOfColumns = new ArrayList<String>(metadata.getColumns().keySet());
        sqlToCSV.createTable(DBMS.getInstance().getActiveDatabase(), tableName,listOfColumns);
        log.info("New table created " + tableName);
    }

    public void updateTable(Map<String, String> updateFields) throws IOException {
        String tableName = updateFields.get("table");
        ArrayList<String> columns = new ArrayList<>();
        HashMap<String,String> set = new HashMap<>();
        set.put(updateFields.get("column"),updateFields.get("value"));
        columns.add(updateFields.get("column"));
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put(updateFields.get("whereColumn"),updateFields.get("whereValue"));
        columns.add(updateFields.get("whereColumn"));

        //semantic check
        if(!metadataManager.tableExists(DBMS.getInstance().getActiveDatabase(),tableName)){
            System.out.println("table " + tableName +"is not present in the table. Please retry");
            return;
        }
        for(String column: columns){
            if(!metadataManager.columnExists(DBMS.getInstance().getActiveDatabase(),tableName,column)){
                System.out.println("column " + column +"is not present in the table. Please retry");
                return;
            }
        }

        if(transaction.checkIfAnyTransactionUsingSametable(tableName)){
            System.out.println("table " + tableName + " is currently locked by other user. please try again later" );
            return;
        }

        transaction.copyTable(DBMS.getInstance().getActiveDatabase(),tableName);

        LocalDateTime startOfQuery  = java.time.LocalDateTime.now();
        //updating to Table
        sqlToCSV.updateTable(DBMS.getInstance().getActiveDatabase(), transaction.getCopyTableName(DBMS.getInstance().getActiveDatabase(),tableName), set,conditions);

        LocalDateTime endOfQuery  = java.time.LocalDateTime.now();
        //transaction
        transaction.insertTransactionLog(tableName, "update",startOfQuery, endOfQuery);

        DBMS.getInstance().getTables().add(tableName);
    }

    public void createDatabase(String databaseName) throws IOException {

        if(metadataManager.databaseExists(DBMS.getInstance().getActiveDatabase())){
            System.out.println("database " + databaseName +"already exists. Please retry");
            return;
        }

        metadataManager.createDatabase( databaseName,DBMS.getInstance().getUsername());
        UserControl.grantAccessToDatabase(DBMS.getInstance().getUsername(),databaseName);
        log.info("New database created " + databaseName);
    }

    public void grantAccess(String databaseName,String username) throws IOException {
        UserControl.grantAccessToDatabase(username,databaseName);
    }

    public void deleteTable(Map<String, String> deleteFields) throws IOException {
        String tableName = deleteFields.get("table");
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put(deleteFields.get("whereColumn"),deleteFields.get("whereValue"));

        //semantic check

        if(!metadataManager.tableExists(DBMS.getInstance().getActiveDatabase(),tableName)){
            System.out.println("table " + tableName +"is not present in the table. Please retry");
            return;
        }

        if(!metadataManager.columnExists(DBMS.getInstance().getActiveDatabase(),tableName,deleteFields.get("whereColumn"))){
            System.out.println("column " + deleteFields.get("whereColumn") +"is not present in the table. Please retry");
            return;
        }

        if(transaction.checkIfAnyTransactionUsingSametable(tableName)){
            System.out.println("table " + tableName + " is currently locked by other user. please try again later" );
            return;
        }

        transaction.copyTable(DBMS.getInstance().getActiveDatabase(),tableName);

        LocalDateTime startOfQuery  = java.time.LocalDateTime.now();
        sqlToCSV.deleteRows(DBMS.getInstance().getActiveDatabase(),transaction.getCopyTableName(DBMS.getInstance().getActiveDatabase(),tableName),conditions);

        LocalDateTime endOfQuery  = java.time.LocalDateTime.now();


        //Transaction Log
        transaction.insertTransactionLog(tableName, "delete",startOfQuery, endOfQuery);

        DBMS.getInstance().getTables().add(tableName);

        log.info("table deleted : " + tableName);
    }

    public void selectTable(Map<String, String> selectFields) throws IOException {
        String tableName = selectFields.get("table");
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put(selectFields.get("whereColumn"),selectFields.get("whereValue"));
        ArrayList<String> columns = new ArrayList<>();
        columns.add(selectFields.get("selectColumn"));

        ArrayList<String> columnsToSemanticCheck = new ArrayList<>();
        columnsToSemanticCheck.add(selectFields.get("selectColumn"));
        columnsToSemanticCheck.add(selectFields.get("whereColumn"));

        //semantic check
        if(!metadataManager.tableExists(DBMS.getInstance().getActiveDatabase(),tableName)){
            System.out.println("table " + tableName +"is not present in the table. Please retry");
            return;
        }

        for(String column: columnsToSemanticCheck){
            if(!metadataManager.columnExists(DBMS.getInstance().getActiveDatabase(),tableName,column)){
                System.out.println("column " + column +"is not present in the table. Please retry");
                return;
            }
        }


        sqlToCSV.selectTable(DBMS.getInstance().getActiveDatabase(),tableName,columns,conditions);
    }

    public void commit(){

        String databaseName = DBMS.getInstance().getActiveDatabase();
        ArrayList<String> tablesArrayList = DBMS.getInstance().getTables();
        Object[] tables = tablesArrayList.toArray();
        String[] str = Arrays
                .copyOf(tables, tables
                                .length,
                        String[].class);

        for(String tableName : str){
            transaction.commit(databaseName,tableName);
        }

    }

    public void rollback(){
        String databaseName = DBMS.getInstance().getActiveDatabase();
        ArrayList<String> tablesArrayList = DBMS.getInstance().getTables();
        Object[] tables = tablesArrayList.toArray();
        String[] str = Arrays
                .copyOf(tables, tables
                                .length,
                        String[].class);

        for(String tableName : str){
            transaction.rollback(databaseName,tableName);
        }
    }

}
