package anon.bidder.adx;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class AdxBidRequestDecryptor {

    private static final int INITIALIZATION_VECTOR_SIZE = 16;
    private static final int SIGNATURE_SIZE = 4;
    private static final int BLOCK_SIZE = 20;

    public static class DecrypterException extends Exception {
          public DecrypterException(String message) {
            super(message);
          }
        }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void main(String args[]){
        byte[] ciphertext = hexStringToByteArray("E2014EA201246E6F6E636520736F7572636501414243C0ADF6B9B6AC17DA218FB50331EDB376701309CAAA01246E6F6E636520736F7572636501414243C09ED4ECF2DB7143A9341FDEFD125D96844E25C3C202466E6F6E636520736F7572636502414243517C16BAFADCFAB841DE3A8C617B2F20A1FB7F9EA3A3600256D68151C093C793B0116DB3D0B8BE9709304134EC9235A026844F276797");
        byte[] encryptionKey = {(byte)0x02, (byte)0xEE, (byte)0xa8, (byte)0x3c, (byte)0x6c, (byte)0x12, (byte)0x11, (byte)0xe1, (byte)0x0b,
                (byte) 0x9f, (byte) 0x88, (byte) 0x96, (byte) 0x6c, (byte) 0xee, (byte) 0xc3, (byte) 0x49, (byte) 0x08, (byte) 0xeb, (byte) 0x94, (byte) 0x6f, (byte) 0x7e,
                (byte) 0xd6, (byte) 0xe4, (byte) 0x41, (byte) 0xaf, (byte) 0x42, (byte) 0xb3, (byte) 0xc0, (byte) 0xf3, (byte) 0x21, (byte) 0x81, (byte) 0x40};
        byte[] integrityKey = {(byte) 0xbf, (byte) 0xFF, (byte) 0xec, (byte) 0x55, (byte) (byte) 0xc3, (byte) 0x01, (byte) 0x30, (byte) 0xc1, (byte) 0xd8,
                (byte) 0xcd, (byte) 0x18, (byte) 0x62, (byte) 0xed, (byte) 0x2a, (byte) 0x4c, (byte) 0xd2, (byte) 0xc7, (byte) 0x6a, (byte) 0xc3, (byte) 0x3b, (byte) 0xc0,
                (byte) 0xc4, (byte) 0xce, (byte) 0x8a, (byte) 0x3d, (byte) 0x3b, (byte) 0xbd, (byte) 0x3a, (byte) 0xd5, (byte) 0x68, (byte) 0x77, (byte) 0x92};
        try {
            byte[] plain = decrypt(ciphertext, new SecretKeySpec(encryptionKey,"HmacSHA1"),new SecretKeySpec(integrityKey,"HmacSHA1"));
        } catch (DecrypterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] decrypt(byte[] ciphertext,
            SecretKey encryptionKey,
            SecretKey integrityKey)
                    throws DecrypterException {
        try {
            // Step 1. find the length of initialization vector and clear text.
            final int plaintext_length =
                    ciphertext.length - INITIALIZATION_VECTOR_SIZE - SIGNATURE_SIZE;
            if (plaintext_length < 0) {
                throw new RuntimeException("The plain text length can't be negative.");
            }

            byte[] iv = Arrays.copyOf(ciphertext, INITIALIZATION_VECTOR_SIZE);

            // Step 2. recover clear text
            final Mac hmacer = Mac.getInstance("HmacSHA1");
            final int ciphertext_end = INITIALIZATION_VECTOR_SIZE + plaintext_length;
            final byte[] plaintext = new byte[plaintext_length];
            boolean add_iv_counter_byte = true;
            for (int ciphertext_begin = INITIALIZATION_VECTOR_SIZE, plaintext_begin = 0;
                    ciphertext_begin < ciphertext_end;) {
                hmacer.reset();
                hmacer.init(encryptionKey);
                final byte[] pad = hmacer.doFinal(iv);

                int i = 0;
                while (i < BLOCK_SIZE && ciphertext_begin != ciphertext_end) {
                    plaintext[plaintext_begin++] =
                            (byte)(ciphertext[ciphertext_begin++] ^ pad[i++]);
                }

                if (!add_iv_counter_byte) {
                    final int index = iv.length - 1;
                    add_iv_counter_byte = ++iv[index] == 0;
                }

                if (add_iv_counter_byte) {
                    add_iv_counter_byte = false;
                    iv = Arrays.copyOf(iv, iv.length + 1);
                }
            }

            // Step 3. Compute integrity hash. The input to the HMAC is clear_text
            // followed by initialization vector, which is stored in the 1st section
            // or ciphertext.
            hmacer.reset();
            hmacer.init(integrityKey);
            hmacer.update(plaintext);
            hmacer.update(Arrays.copyOf(ciphertext, INITIALIZATION_VECTOR_SIZE));
            final byte[] computedSignature = Arrays.copyOf(hmacer.doFinal(), SIGNATURE_SIZE);
            final byte[] signature = Arrays.copyOfRange(
                    ciphertext, ciphertext_end, ciphertext_end + SIGNATURE_SIZE);
            if (!Arrays.equals(signature, computedSignature)) {
                throw new DecrypterException("Signature mismatch.");
            }
            return plaintext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("HmacSHA1 not supported.", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Key is invalid for this purpose.", e);
        }
    }
}
