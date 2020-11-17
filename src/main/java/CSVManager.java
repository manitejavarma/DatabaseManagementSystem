import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {

    public static void readCSV() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv"));
        Reader reader = new InputStreamReader(new BOMInputStream(is), "UTF-8");
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        //Note : Mani, get header as parameter and do empty check
        ArrayList<String> header = new ArrayList<>();
        header.add("A");
        header.add("B");
        header.add("C");
        header.add("D");
        for (CSVRecord record : csvParser) {
            for (String headerStr : header) {
                System.out.print(record.get(headerStr) + " ");
            }
            System.out.println();
        }
    }

    public static void insertCSV() throws IOException {
        ArrayList<String> allHeaders = new ArrayList<>();
        allHeaders.add("A");
        allHeaders.add("B");
        allHeaders.add("C");
        allHeaders.add("D");

        ArrayList<String> header = new ArrayList<>();
        header.add("A");
        header.add("C");
        header.add("D");

        ArrayList<String> allValues = new ArrayList<>(allHeaders.size());
        for (int i = 0; i < allHeaders.size(); i++) {
            allValues.add("");
        }
        for (int i = 0; i < header.size(); i++) {
            allValues.set(allHeaders.indexOf(header.get(i)), "1");
        }

        BufferedWriter writer = Files.newBufferedWriter(
                Paths.get("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv"),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord(allValues);
        csvPrinter.flush();
    }

    public static void getHeaders() throws IOException {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT;
        FileReader fileReader = new FileReader("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv");
        CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
        List csvRecords = csvFileParser.getHeaderNames();
        System.out.println(csvRecords.get(0));
    }

    public static void setHeaders() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv"));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        ArrayList<String> allHeaders = new ArrayList<>();
        allHeaders.add("A");
        allHeaders.add("B");
        allHeaders.add("C");
        allHeaders.add("D");

        csvPrinter.printRecord(allHeaders);
        csvPrinter.flush();
    }

    public static void updateCSV() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv"));
        Reader reader = new InputStreamReader(new BOMInputStream(is), "UTF-8");
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        List<String[]> tableData = new ArrayList<>();

        for(CSVRecord csvRecord: csvParser.getRecords()){
            String[] recordStr = new String[csvRecord.size()];
            for(int i = 0;i< csvRecord.size();i++){
                recordStr[i] = csvRecord.get(i);
            }
            tableData.add(recordStr);
        }


        tableData.set(1,new String[]{"Mani","Teja","Varma","K"});

        //write to file
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv"),StandardOpenOption.WRITE);

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecords(tableData);
        csvPrinter.flush();
    }

}