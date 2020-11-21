import tech.tablesaw.api.Table;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        String username = ReadInput.readInput("Please enter username");
        String password = ReadInput.readInput("Please enter password");

        Transaction transaction = new Transaction();
        transaction.generateTransactionId(username);

        SQLToCSV sqlToCSV = new SQLToCSV();

        //create copy of file
        String tableName = "temp";
        String copyTableName = transaction.getId()+"_"+tableName;
        Table table = Table.read().csv("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+tableName+".csv");
        table.write().csv("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+copyTableName+".csv");
        sqlToCSV.insertQuery(copyTableName);

        String stmt = ReadInput.readInput("Commit or rollback");
        if(stmt.equals("commit")){
            try{
                File mainFile = new File("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+tableName+".csv");
                File copyFile = new File("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+copyTableName+".csv");

                replace(mainFile,copyFile);
            }catch (Exception e){
                System.out.println(e);
            }
        }else if(stmt.equals("rollback")){
            File copyFile = new File("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+copyTableName+".csv");
            copyFile.delete();
        }
        transaction.generateTransactionId(username);
    }

    //Citation : https://www.codota.com/code/java/methods/java.io.File/renameTo
    protected static void replace(File dst, File backup) throws IOException {
        dst.delete();
        if(!backup.renameTo(dst)) {
            throw new IOException("Failed to rename "+backup+" to "+dst);
        }
    }
}
