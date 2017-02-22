package tools;

public interface SystemProps {
	String PATH_TO_HOME = System.getProperty("user.home");
	String FILE_SEPARATOR = System.getProperty("file.separator");
	String PATH_SEPARATOR = System.getProperty("path.separator");
	String LINE_SEPARATOR = System.getProperty("line.separator");
	String RESULTS_NAME = "Coinsearch_results.txt";
	String FULL_PATH_TO_RESULTS_NAME = PATH_TO_HOME + FILE_SEPARATOR + RESULTS_NAME;
	String LOG_FILENAME = "Coinsearch_Logfile.txt";
	String FULL_PATH_TO_LOGFILE = PATH_TO_HOME + FILE_SEPARATOR + LOG_FILENAME;
}