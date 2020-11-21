import tech.tablesaw.api.CategoricalColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//using tablesaw to do csv operations based on SQL query
public class NewCSVManager {

    public static void readCSV(String filepath, HashMap<String,String> conditions, ArrayList<String> columns) throws IOException {
        Table table = Table.read().csv(filepath);
        //conditions.forEach((k, v) -> table.where(table.stringColumn(k).isEqualTo(v)));
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            table = table.where(table.stringColumn(entry.getKey()).isEqualTo(entry.getValue()));
        }
        Table reduced = table.select(columns.toArray(new String[columns.size()]));
        for (Row row : reduced) {
            for(String column: columns){
                System.out.println(row.getString(column));
            }
        }
    }

    public static void updateCSV(String filepath, HashMap<String,String> conditions, HashMap<String,String> set) throws IOException{
            Table table = Table.read().csv(filepath);
            for (Row row : table) {
                for (Map.Entry<String, String> entry : conditions.entrySet()) {
                    if(row.getString(entry.getKey()).equals(entry.getValue())){
                        for (Map.Entry<String, String> entry2 : set.entrySet()) {
                            row.setString(entry2.getKey(), entry2.getValue());
                        }
                    }
                }
            }
        table.write().csv(filepath);
    }

    public static void insertCSV(String filepath, HashMap<String,String> columnsAndValues) throws IOException{
        Table table = Table.read().csv(filepath);
        ArrayList<String> headers = (ArrayList<String>) table.columnNames();
        Table temp = Table.create("temp table");

        for(String header: headers){
            if(columnsAndValues.containsKey(header)){
                temp.addColumns(StringColumn.create(header, new String[] {columnsAndValues.get(header)}));
            }else{
                temp.addColumns(StringColumn.create(header, new String[] {""}));
            }
        }

        table.addRow(0,temp);
        table.write().csv(filepath);

    }

    public static void alterAddColumn(String filepath, ArrayList<String> columnNames) throws IOException{
        Table table = Table.read().csv(filepath);
        String [] s = new String[table.rowCount()];
        java.util.Arrays.fill(s,"");
        for(String column: columnNames){
                table.addColumns(StringColumn.create(column, s));
        }
        table.write().csv(filepath);
    }

    public static void alterRemoveColumn(String filepath, ArrayList<String> columnNames) throws IOException{
        Table table = Table.read().csv(filepath);
        for(String column: columnNames){
            table.removeColumns(column);
        }
        table.write().csv(filepath);
    }


    public static void createTableWithHeaders(String filepath,ArrayList<String> headers) throws IOException {
        Table temp = Table.create("new Table");
        for(String header:headers){
            temp.addColumns(StringColumn.create(header, new String[] {""}));
        }
        temp = temp.dropRange(0,1);
        temp.write().csv(filepath);
    }
}
