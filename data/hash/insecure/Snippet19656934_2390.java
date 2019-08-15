package passwordcracking;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Encrypt implements Runnable {

private static final Logger LOGGER = Logger.getLogger("passwordCracker");
private final Buffers<String> bufferTakeFrom;
private final Buffers<PairEncPWandClearPW> bufferPutTo;
String possiblePassword;
private MessageDigest messageDigest;

/**
 *
 * @param bufferTakeFrom
 */
public Encrypt(Buffers<String> bufferTakeFrom, Buffers<PairEncPWandClearPW>        

bufferPutTo) 
{
    this.bufferTakeFrom = bufferTakeFrom;
    this.bufferPutTo = bufferPutTo;
    try {
        messageDigest = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage());
        throw new RuntimeException(ex);
    }
}

@Override
public void run() {
    do {
        possiblePassword = bufferTakeFrom.take();
        EncryptSingleWord(possiblePassword);
    } while (!possiblePassword.equals("-1"));

}

private void EncryptSingleWord(final String possiblePassword) {
    byte[] digest = null;
    try {
        digest = messageDigest.digest(possiblePassword.getBytes());
        PairEncPWandClearPW pair = new PairEncPWandClearPW(digest, possiblePassword);
        bufferPutTo.put(pair);
    } catch (Exception ex) {
        System.out.println("Exception: " + ex);
        System.out.println("possible password bytes: " + possiblePassword.getBytes());
        System.out.println("password:" + possiblePassword);

    }

}}
