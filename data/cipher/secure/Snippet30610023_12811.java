import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public final static int SALT_LEN     = 8;
    static final String     HEXES        = "0123456789ABCDEF";
    String                  mPassword    = null;
    byte[]                  mInitVec     = null;
    byte[]                  mSalt        = new byte[SALT_LEN];
    Cipher                  mEcipher     = null;
    Cipher                  mDecipher    = null;
    private final int       KEYLEN_BITS  = 128;    // see notes below where this is used.
    private final int       ITERATIONS   = 65536;
    private final int       MAX_FILE_BUF = 1024;

    /**
     * create an object with just the passphrase from the user. Don't do anything else yet
     * @param password
     */
    public AES(String password) {
        mPassword = password;
    }

    public static String byteToHex(byte[] raw) {
        if (raw == null) {
            return null;
        }

        final StringBuilder hex = new StringBuilder(2 * raw.length);

        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }

        return hex.toString();
    }

    public static byte[] hexToByte(String hexString) {
        int    len = hexString.length();
        byte[] ba  = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            ba[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                + Character.digit(hexString.charAt(i + 1), 16));
        }

        return ba;
    }

    /**
     * debug/print messages
     * @param msg
     */
    private void Db(String msg) {
        System.out.println("** Crypt ** " + msg);
    }

    /**
     * This is where we write out the actual encrypted data to disk using the Cipher created in setupEncrypt().
     * Pass two file objects representing the actual input (cleartext) and output file to be encrypted.
     *
     * there may be a way to write a cleartext header to the encrypted file containing the salt, but I ran
     * into uncertain problems with that.
     *
     * @param input - the cleartext file to be encrypted
     * @param output - the encrypted data file
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public void WriteEncryptedFile(InputStream inputStream, OutputStream outputStream)
            throws IOException, IllegalBlockSizeException, BadPaddingException {
        try {
            long             totalread = 0;
            int              nread     = 0;
            byte[]           inbuf     = new byte[MAX_FILE_BUF];
            SecretKeyFactory factory   = null;
            SecretKey        tmp       = null;

            // crate secureRandom salt and store  as member var for later use
            mSalt = new byte[SALT_LEN];

            SecureRandom rnd = new SecureRandom();

            rnd.nextBytes(mSalt);
            Db("generated salt :" + byteToHex(mSalt));
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            /*
             *  Derive the key, given password and salt.
             *
             * in order to do 256 bit crypto, you have to muck with the files for Java's "unlimted security"
             * The end user must also install them (not compiled in) so beware.
             * see here:  http://www.javamex.com/tutorials/cryptography/unrestricted_policy_files.shtml
             */
            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);

            tmp = factory.generateSecret(spec);

            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            /*
             *  Create the Encryption cipher object and store as a member variable
             */
            mEcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mEcipher.init(Cipher.ENCRYPT_MODE, secret);

            AlgorithmParameters params = mEcipher.getParameters();

            // get the initialization vectory and store as member var
            mInitVec = params.getParameterSpec(IvParameterSpec.class).getIV();
            Db("mInitVec is :" + byteToHex(mInitVec));
            outputStream.write(mSalt);
            outputStream.write(mInitVec);

            while ((nread = inputStream.read(inbuf)) > 0) {
                Db("read " + nread + " bytes");
                totalread += nread;

                // create a buffer to write with the exact number of bytes read. Otherwise a short read fills inbuf with 0x0
                // and results in full blocks of MAX_FILE_BUF being written.
                byte[] trimbuf = new byte[nread];

                for (int i = 0; i < nread; i++) {
                    trimbuf[i] = inbuf[i];
                }

                // encrypt the buffer using the cipher obtained previosly
                byte[] tmpBuf = mEcipher.update(trimbuf);

                // I don't think this should happen, but just in case..
                if (tmpBuf != null) {
                    outputStream.write(tmpBuf);
                }
            }

            // finalize the encryption since we've done it in blocks of MAX_FILE_BUF
            byte[] finalbuf = mEcipher.doFinal();

            if (finalbuf != null) {
                outputStream.write(finalbuf);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();
            outputStream.close();
            Db("wrote " + totalread + " encrypted bytes");
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidParameterSpecException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read from the encrypted file (input) and turn the cipher back into cleartext. Write the cleartext buffer back out
     * to disk as (output) File.
     *
     * I left CipherInputStream in here as a test to see if I could mix it with the update() and final() methods of encrypting
     *  and still have a correctly decrypted file in the end. Seems to work so left it in.
     *
     * @param input - File object representing encrypted data on disk
     * @param output - File object of cleartext data to write out after decrypting
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public void ReadEncryptedFile(InputStream inputStream, OutputStream outputStream)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        try {
            CipherInputStream cin;
            long              totalread = 0;
            int               nread     = 0;
            byte[]            inbuf     = new byte[MAX_FILE_BUF];

            // Read the Salt
            inputStream.read(this.mSalt);
            Db("generated salt :" + byteToHex(mSalt));

            SecretKeyFactory factory = null;
            SecretKey        tmp     = null;
            SecretKey        secret  = null;

            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);

            tmp    = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            /* Decrypt the message, given derived key and initialization vector. */
            mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Set the appropriate size for mInitVec by Generating a New One
            AlgorithmParameters params = mDecipher.getParameters();

            mInitVec = params.getParameterSpec(IvParameterSpec.class).getIV();

            // Read the old IV from the file to mInitVec now that size is set.
            inputStream.read(this.mInitVec);
            Db("mInitVec is :" + byteToHex(mInitVec));
            mDecipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(mInitVec));

            // creating a decoding stream from the FileInputStream above using the cipher created from setupDecrypt()
            cin = new CipherInputStream(inputStream, mDecipher);

            while ((nread = cin.read(inbuf)) > 0) {
                Db("read " + nread + " bytes");
                totalread += nread;

                // create a buffer to write with the exact number of bytes read. Otherwise a short read fills inbuf with 0x0
                byte[] trimbuf = new byte[nread];

                for (int i = 0; i < nread; i++) {
                    trimbuf[i] = inbuf[i];
                }

                // write out the size-adjusted buffer
                outputStream.write(trimbuf);
            }

            outputStream.flush();
            cin.close();
            inputStream.close();
            outputStream.close();
            Db("wrote " + totalread + " encrypted bytes");
        } catch (Exception ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * adding main() for usage demonstration. With member vars, some of the locals would not be needed
     */
    public static void main(String[] args) {

        // create the input.txt file in the current directory before continuing
        File   input   = new File("input.txt");
        File   eoutput = new File("encrypted.aes");
        File   doutput = new File("decrypted.txt");
        String iv      = null;
        String salt    = null;
        AES    en      = new AES("mypassword");

        /*
         * write out encrypted file
         */
        try {
            en.WriteEncryptedFile(new FileInputStream(input), new FileOutputStream(eoutput));
            System.out.printf("File encrypted to " + eoutput.getName() + "\niv:" + iv + "\nsalt:" + salt + "\n\n");
        } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }

        /*
         * decrypt file
         */
        AES dc = new AES("mypassword");

        /*
         * write out decrypted file
         */
        try {
            dc.ReadEncryptedFile(new FileInputStream(eoutput), new FileOutputStream(doutput));
            System.out.println("decryption finished to " + doutput.getName());
        } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }
    }
}
