import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedLogger {

private static Date lastLogTime = null;
private static EncryptedLogger instance = null;
private static FileOutputStream fos = null;
private static CipherOutputStream cos = null;
private static PrintWriter writer = null;
private Cipher cipher;
byte[] Key ={(byte) 0x12,(byte) 0x34,0x55,(byte) 0x66,0x67,(byte)0x88,(byte)0x90,0x12,(byte) 0x23,0x45,0x67,(byte)0x89,0x12,0x33,(byte) 0x55,0x74};

public static EncryptedLogger getInstance(){
    if (instance==null) {
        instance = new EncryptedLogger();
    }
    return instance;
}

private EncryptedLogger(){

    class SQLShutdownHook extends Thread{
        @Override
        public void run() {
            EncryptedLogger.close();
            super.run();
        }
    }

    SecretKeySpec sks = new SecretKeySpec(Key,"AES");
    try {
        cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE,sks);

        fos = new FileOutputStream(new File("log.txt"),true);
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    cos = new CipherOutputStream(fos, cipher);
    writer = new PrintWriter(cos);

    SQLShutdownHook hook = new SQLShutdownHook();
    Runtime.getRuntime().addShutdownHook(hook);
}

public synchronized void logSQL(String s){
    if ((lastLogTime==null)||((new Date().getTime() -lastLogTime.getTime())>1000)){
        lastLogTime = new Date();
        writer.printf("-- %1$tm-%1$te-%1$tY %1$tH-%1$tM-%1$tS\n%2$s\n",new Date(),s);   
    }
    else{
        writer.println(s);
    }
}

public synchronized void logComment(String s){
    writer.printf("-- %1$tm-%1$te-%1$tY %1$tH-%1$tM-%1$tS: %2$s\n",new Date(),s);
}

public static void close(){
    writer.flush();
    writer.close();
}

public static void main(String[] args) throws InterruptedException {
    EncryptedLogger.getInstance().logSQL("1");
    EncryptedLogger.getInstance().logSQL("22");
    EncryptedLogger.getInstance().logSQL("33333");
    EncryptedLogger.getInstance().logSQL("4900");
    EncryptedLogger.getInstance().logSQL("5");
    EncryptedLogger.getInstance().logSQL("66666");
    EncryptedLogger.getInstance().logSQL("Some test logging statement");
    EncryptedLogger.getInstance().logSQL("AAAAAAAAAAAAAAAAAAAAAAAAAA");
    EncryptedLogger.getInstance().logComment("here is test commentary");
}

}
