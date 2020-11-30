package DBMS;

import DBMS.attributetype.AttributeType;
import DBMS.metadata.Metadata;
import DBMS.metadata.MetadataManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {


        MetadataManager metadataManager = new MetadataManager();
        System.out.println(metadataManager.tableExists("TestDB","test"));

//        SqlParser sqlParser = new SqlParser();
//        Transaction transaction = new Transaction();
//        String username = "root";//ReadInput.readInput("Please enter username");
//        String password = "root";//ReadInput.readInput("Please enter password");
//
//        DBMS dbms = DBMS.getInstance();
//        //if valid user
//        dbms.setUsername(username);
//        transaction.generateTransactionId(username);
//        dbms.setTransactionId(transaction.getId());
//        String query = "use TestDB;";//ReadInput.readInput("Enter Query");
//        do{
//            sqlParser.validateQuery(query);
//            query = ReadInput.readInput("Enter Query");
//        }while(query!="quit");


//
//        User user = new User(username,password);
//        if(!UserControl.userExists(user.getUsername())){
//            throw new IllegalArgumentException("DBMS.User doesn't exist");
//        }
//        //Query: use test
//        String databaseName = "test";
//        if(!UserControl.doesUserHaveAccessToDB(user.getUsername(),databaseName)){
//            System.out.println("DBMS.User doesn't have access to DB!");
//        }
//
//
//        //GRANT ALL ON [database_name].* TO '[user_name]'
//        if(!UserControl.grantAccessToDatabase("mani","test2")){
//            System.out.println("Something went wrong.Please try again later!");
//        }

//        MetadataManager metadataManager = new MetadataManager();
//        metadataManager.createDatabase("tes4","mani");
//
//        MetadataManager metadataManager = new MetadataManager();
//        metadataManager.createTableMetadata("tes4","mani");

//        MetadataManager metadataManager = new MetadataManager();
//        metadataManager.getMetadataByDatabaseAndTable("CollegeDatabase","course");

        //createStatement();
    }

    private static void createStatement() {
        MetadataManager metadataManager = new MetadataManager();
        Metadata metadata = metadataManager.getMetadataByDatabaseAndTable("CollegeDatabase","course");
        String createStatement = "";
        createStatement = createStatement + "create table " + metadata.getTableName() + "(";
        for (Map.Entry<String, AttributeType> entry2 : metadata.getColumns().entrySet()) {
            createStatement = createStatement + entry2.getKey() + " " + entry2.getValue().toString() + ",";
        }
        if(metadata.getPrimaryKeys().size()>0){
            createStatement = createStatement + String.join(",",metadata.getPrimaryKeys());
        }
        System.out.println(createStatement);
    }
}
