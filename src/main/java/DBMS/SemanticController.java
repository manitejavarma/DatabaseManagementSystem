package DBMS;

import DBMS.attributetype.AttributeType;
import DBMS.metadata.Metadata;
import DBMS.metadata.MetadataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticController {

    SQLToCSV sqlToCSV;
    MetadataManager metadataManager;

    SemanticController(){
        this.sqlToCSV = new SQLToCSV();
        metadataManager = new MetadataManager();
    }

    public void insert(Map<String, String[]> insertFields) throws IOException {
        String tableName = insertFields.get("table")[0];
        String[] columns = insertFields.get("columns");
        String[] values = insertFields.get("values");
        String[] newValues = new String[values.length];
        for(int i =0;i<values.length;i++){
            newValues[i] = values[i].replace("\"", "");
        }

        HashMap<String,String> columnsAndValues = new HashMap<>();
        if(columns[0].isEmpty()){
            //Setting columns and values to insert into CSV
            List<String> allTableColumns = this.sqlToCSV.getHeaders(DBMS.getInstance().getActiveDatabase(),tableName);

            for(int i =0;i<allTableColumns.size();i++){
                columnsAndValues.put(allTableColumns.get(i),newValues[i]);
            }
        }else{
            //Setting columns and values to insert into CSV
            for(int i =0;i<columns.length;i++){
                columnsAndValues.put(columns[i],newValues[i]);
            }
        }
        sqlToCSV.insertQuery(DBMS.getInstance().getActiveDatabase(), tableName,columnsAndValues);
    }

    public void createTable(Metadata metadata) throws IOException {

        String tableName = metadata.getTableName();

        metadataManager.createTableMetadata(DBMS.getInstance().getActiveDatabase(), tableName,metadata);
        ArrayList<String> listOfColumns = new ArrayList<String>(metadata.getColumns().keySet());
        sqlToCSV.createTable(DBMS.getInstance().getActiveDatabase(), tableName,listOfColumns);
    }
}
