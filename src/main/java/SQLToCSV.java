import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLToCSV {

    public void insertQuery(String tablename) throws IOException {
        HashMap<String,String> columnsAndValues = new HashMap<>();
        columnsAndValues.put("A","Mani");
        columnsAndValues.put("C","Teja");
        columnsAndValues.put("D","Varma");
        NewCSVManager.insertCSV("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+tablename+".csv",columnsAndValues);
    }

    public void doAction() throws IOException {
        //select statement goes here
        String filepath = "E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv";
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("C","this");
        ArrayList<String> columns = new ArrayList<>();
        columns.add("A");
        columns.add("B");
        columns.add("C");
        columns.add("D");
        NewCSVManager.readCSV("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv",conditions,columns);


        //updateCSV . Mani, please fix the bug of writing to same file
        HashMap<String,String> set = new HashMap<>();
        set.put("D","whaa");
        NewCSVManager.updateCSV("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv",conditions,set);


        //insertCSV
        HashMap<String,String> columnsAndValues = new HashMap<>();
        columnsAndValues.put("A","Mani");
        columnsAndValues.put("C","Teja");
        columnsAndValues.put("D","Varma");
        NewCSVManager.insertCSV("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv",columnsAndValues);


        //create new file with headers
        NewCSVManager.createTableWithHeaders("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp3.csv",columns);

        //alter table add new column
        ArrayList<String> columnList = new ArrayList<>();
        columnList.add("E");
        NewCSVManager.alterAddColumn("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv",columnList);

        //alter table remove new column
        NewCSVManager.alterRemoveColumn("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv",columnList);
    }
}
