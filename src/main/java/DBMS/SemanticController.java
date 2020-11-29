package DBMS;

import java.util.Map;

public class SemanticController {

    public static void insert(Map<String, String[]> insertFields){
        String tableName = insertFields.get("table")[0];
        String[] columns = insertFields.get("columns");
        String[] values = insertFields.get("values");
        String[] newValues = new String[values.length];
        for(int i =0;i<values.length;i++){
            newValues[i] = values[i].replace("\"", "");
        }


        if(columns[0].isEmpty()){
            //get all headers
        }else{

        }

    }
}
