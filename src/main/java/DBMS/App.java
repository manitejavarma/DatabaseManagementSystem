package DBMS;

import DBMS.metadata.MetadataManager;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

//        String username = ReadInput.readInput("Please enter username");
//        String password = ReadInput.readInput("Please enter password");
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

        MetadataManager metadataManager = new MetadataManager();
        metadataManager.createTableMetadata("tes4","mani");
    }
}
