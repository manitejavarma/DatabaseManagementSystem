//https://regexlib.com/REDetails.aspx?regexp_id=945&AspxAutoDetectCookieSupport=1
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Constants;

public class SqlParser implements ISqlParser {
	private final static Logger logger = Logger.getLogger(SqlParser.class.getName());
    
	public int validateQuery(String query) {
		logger.info("Invoked validateQuery method");
		Pattern pattern = Pattern.compile(Constants.REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		boolean matchFound = matcher.find();
		if (matchFound) {
			return Constants.ONE;
		} else {
			return Constants.ZERO;
		}

	}

}
