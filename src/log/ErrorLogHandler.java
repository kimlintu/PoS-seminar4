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
	private final String filepath;
	private PrintWriter writer;

	/**
	 * Constructs a new instance to a standard file path specified in this class.
	 * 
	 * @throws IOException if the named file exists but is a directory rather than a
	 *                     regular file, does not exist but cannot be created, or
	 *                     cannot be opened for any other reason
	 */
	public ErrorLogHandler() throws IOException {
		this.filepath = "pos-error-log.txt";
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filepath, true)));
	}

	/**
	 * Constructs a new instance that writes to the file specified with
	 * <code>path</code>.
	 * 
	 * @param path Path to the file that should be written to.
	 * @throws IOException if the named file exists but is a directory rather than a
	 *                     regular file, does not exist but cannot be created, or
	 *                     cannot be opened for any other reason.
	 */
	public ErrorLogHandler(String path) throws IOException {
		this.filepath = path;
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filepath, true)));
	}

	/**
	 * Prints the specified exception's error message, followed by the stack trace,
	 * to the log file. If text already exists in the log, the message will be appended
	 * and printed on a new line.
	 * 
	 * @param exceptionToLog The exception that should be logged.
	 */
	public void logException(Exception exceptionToLog) {
		StringBuilder sb = new StringBuilder("[" + getLocalTimeAndDate() + "]\n");
		sb.append("ERROR:\n");
		sb.append(exceptionToLog.getMessage() + "\n");
		sb.append("Stack trace:");
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
