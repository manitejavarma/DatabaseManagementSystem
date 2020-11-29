import java.util.logging.Logger;

import DBMS.ReadInput;
import utils.Constants;

public class App {
	private final static Logger logger = Logger.getLogger(App.class.getName());
    public static void main(String[] args){
    	logger.info("Application started");
        ISqlParser parser = new SqlParser();
        String query = ReadInput.readInput("Insert Query >");
        parser.validateQuery(query);
    }
}
