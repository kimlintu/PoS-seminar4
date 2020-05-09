package log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Outputs the exception's error message to the log file.
 */
public class ErrorLogHandler {
	private final String LOG_FILE_PATH = "pos-error-log.txt";
	private PrintWriter writer;

	public ErrorLogHandler() throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE_PATH, true)));
	}

	/**
	 * Prints the specified exception's error message, followed by the stack trace,
	 * to the log file.
	 * 
	 * @param exceptionToLog The exception that should be logged.
	 */
	public void logException(Exception exceptionToLog) {
		StringBuilder sb = new StringBuilder("[" + getLocalTimeAndDate() + "]\n");
		sb.append("ERROR:\n");
		sb.append(exceptionToLog.getMessage() + "\n");
		sb.append("Stack trace: \n");
		writer.println(sb.toString());
		exceptionToLog.printStackTrace(writer);
		writer.println();
		
		writer.flush();
	}
	
	private String getLocalTimeAndDate() {
		DateTimeFormatter dateAndTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		
		return LocalDateTime.now().format(dateAndTime);
	}
}
