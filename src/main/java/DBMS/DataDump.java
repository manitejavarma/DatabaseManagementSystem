package DBMS;

import DBMS.attributetype.AttributeType;
import DBMS.metadata.Metadata;
import DBMS.metadata.MetadataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataDump {

    NewCSVManager newCSVManager;

    DataDump(){
        newCSVManager = new NewCSVManager();
    }

    private void insertValueDump(String databaseName, String tableName) throws IOException {
        String insertquerystatements = "";
        ArrayList<ArrayList<String>> data = newCSVManager.getAllDataFromTable("database/"+databaseName+"/"+tableName+".csv");
        for(ArrayList<String> row : data){
            insertquerystatements = insertquerystatements + "insert into table " + tableName + " values (";
            insertquerystatements = insertquerystatements + join(row) + ");";
        }
    }

    private void createTableDump(String databaseName,String tableName) {
        MetadataManager metadataManager = new MetadataManager();
        Metadata metadata = metadataManager.getMetadataByDatabaseAndTable(databaseName,tableName);
        String createStatement = "";
        createStatement = createStatement + "create table " + metadata.getTableName() + "(";
        for (Map.Entry<String, AttributeType> entry2 : metadata.getColumns().entrySet()) {
            createStatement = createStatement + entry2.getKey() + " " + entry2.getValue().toString() + ",";
        }
        if(metadata.getPrimaryKeys().size()>0){
            createStatement = createStatement + String.join(",",metadata.getPrimaryKeys());
        }
    }

    private String join(List<String> namesList) {
        return String.join(",", namesList
                .stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()));
    }
}
