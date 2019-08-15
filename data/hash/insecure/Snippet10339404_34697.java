import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 *  The MyLogger class abstracts the writing of log messages to a file.
 *  This is a synchronized implementation due to the usage of java.nio.channels.FileChannel 
 *  which is used to write log messages to the log file.
 *  
 *  The MyLogger class maintains a HashMap of MyLogger instances per log file.  
 *  The Key is the MD5 hash of the log file path and the Value is the MyLogger instance for that log file.
 *
 */
public final class MyLogger {
    private static final int BUFFER_SIZE = 1024;
    private static final int DIGEST_BASE_RADIX = 16;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static HashMap<String, MyLogger> sLoggerMap;

    private FileChannel mLogOutputChannel;
    private ByteBuffer mByteBuffer;
    private String mLogDir;
    private String mLogFileName;

    /**
     * Private constructor which creates our log dir and log file if they do not already already exist. 
     * If the log file exists, then it is opened in append mode.
     * 
     * @param logDir
     *            The dir where the log file resides
     * @param logFileName
     *            The file name of the log file
     * @throws IOException
     *             Thrown if the file could not be created or opened for writing.
     */
    private MyLogger(String logDir, String logFileName) throws IOException {
        mLogDir = logDir;
        mLogFileName = logFileName;

        // create the log dir and log file if they do not exist
        FileOutputStream logFile;
        new File(mLogDir).mkdirs();

        final String logFilePath = mLogDir + File.separatorChar + mLogFileName;
        final File f = new File(logFilePath);
        if(!f.exists()) {
            f.createNewFile();
        }
        logFile = new FileOutputStream(logFilePath, true);

        // set up our output channel and byte buffer  
        mLogOutputChannel = logFile.getChannel();
        mByteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    /**
     * Writes the given log message to the log file that is represented by this MyLogger instance. 
     * If the log message could not be written to the log file an error is logged in the System log.
     * 
     * @param logMessage
     *            The log message to write to the log file.
     */
    public void log(String logMessage) {

        // write the log message to the log file
        if (mLogOutputChannel != null) {
            mByteBuffer.put(logMessage.getBytes());
            mByteBuffer.put(LINE_SEPARATOR.getBytes());
            mByteBuffer.flip();
            try {
                mLogOutputChannel.write(mByteBuffer);
                // ensure that the data we just wrote to the log file is pushed to the disk right away
                mLogOutputChannel.force(true);
            } catch (IOException e) {
                // Could not write to log file output channel
                e.printStackTrace();
                return;
            }
        }

        if(mByteBuffer != null) {
            mByteBuffer.clear();
        }
    }

    /**
     * Get an instance of the MyLogger for the given log file. Passing in the same logDir and logFileName will result in the same MyLogger instance being returned.
     * 
     * @param logDir
     *            The directory path where the log file resides. Cannot be empty or null.
     * @param logFileName
     *            The name of the log file Cannot be empty or null.
     * @return The instance of the MyLogger representing the given log file. Null is returned if either logDir or logFilename is null or empty string.
     * @throws IOException
     *             Thrown if the file could not be created or opened for writing.
     */
    public static MyLogger getLog(String logDir, String logFileName) throws IOException {
        if(logDir == null || logFileName == null || logDir.isEmpty() || logFileName.isEmpty()) {
            return null;
        }

        if(sLoggerMap == null) {
            sLoggerMap = new HashMap<String, MyLogger>();
        }

        final String logFilePathHash = getHash(logDir + File.separatorChar + logFileName);
        if(!sLoggerMap.containsKey(logFilePathHash)) {
            sLoggerMap.put(logFilePathHash, new MyLogger(logDir, logFileName));
        }

        return sLoggerMap.get(logFilePathHash);
    }

    /**
     * Utility method for generating an MD5 hash from the given string.
     * 
     * @param path
     *            The file path to our log file
     * @return An MD5 hash of the log file path. If an MD5 hash could not be generated, the path string is returned.
     */
    private static String getHash(String path) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(path.getBytes());
            return new BigInteger(digest.digest()).toString(DIGEST_BASE_RADIX);
        } catch (NoSuchAlgorithmException ex) {
            // this should never happen, but just to make sure return the path string
            return path;
        }
    }
}
