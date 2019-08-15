package uk.co.mysterymayhem.bukkittestplugin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.bukkit.Bukkit;

public class LogFilter implements Filter {

  private final HashSet<List<Byte>> previousErrors;

  public LogFilter() {
    previousErrors = new HashSet<>();
  }

  @Override
  public boolean isLoggable(LogRecord record) {

    // Debug message
    Bukkit.getLogger().log(Level.INFO, "{0} [number {1}:{2}]",
            new Object[]{record.toString(), record.getSequenceNumber(),
              record.getLoggerName()});

    // Prepare empty string
    String currentMessage = "";
    try {
      for (StackTraceElement element :
              record.getThrown().getCause().getStackTrace()) {
        currentMessage += element.toString() + "\n";
      }
    } catch (NullPointerException e) {
      // No exception or no exception cause, unlikely to be spammed, so return
      // early
      return true;
    }

    // Prepare List
    List<Byte> currentHashList = new ArrayList<>();
    try {
      // Create MD5 hash of exception cause
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] currentHash = digest.digest(currentMessage.getBytes("UTF-8"));

      // Add bytes to Byte list
      for (byte element : currentHash) {
        currentHashList.add(element);
      }
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
      // Shh the compiler
    }
    // Try and add this list of Bytes, false if already found, true if new
    // (just return this when working)
    boolean shouldLog = previousErrors.add(currentHashList);
    // Debug output of shouldLog value
    Bukkit.getLogger().log(Level.INFO, "\n\n{0}\n\n", shouldLog);
    return shouldLog;
    //return previousErrors.add(currentHashList);
    //return currentHashList.isEmpty() || previousErrors.add(currentHashList);
  }
}
