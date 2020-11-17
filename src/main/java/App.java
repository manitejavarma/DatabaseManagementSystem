import org.apache.commons.csv.CSVRecord;

import java.io.IOException;

public class App {
    public static void main(String[] args){
        System.out.println("Hello World!");
        try {
            CSVManager.setHeaders();
          CSVManager.insertCSV();
            CSVManager.insertCSV();
            //CSVManager.readCSV();
//            CSVManager.getHeaders();
            //CSVManager.setHeaders();
            CSVManager.updateCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
