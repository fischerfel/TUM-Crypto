import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;


public class ABCHash{
        public static void main(String[] args) throws Exception        
        {
                try{
                        int character;
                        StringBuffer buffer = new StringBuffer("");
                        FileInputStream inputStream = new FileInputStream(new File("C:/EncPDF.txt"));

                        while( (character = inputStream.read()) != -1)
                                buffer.append((char) character);

                        inputStream.close();
                        System.out.println("Fetching data from the file"+buffer);

                        StringBuffer sbuf = new StringBuffer(buffer);

                        String str=sbuf.toString();

                        System.out.println( "Data = "+ str);
                        if(str!=null)
                        {
                                String key = "0123456789ABCDEF0123456789ABCDEF"; // Assuming the key as 0123456789ABCDEF

                                byte[] hexvalue= stringToHexByte(str);
                                byte[] hexkey=stringToHexByte(key);
                                byte[] byHMAC = encode(hexkey, hexvalue);
                                String stEncryptedData = Hex.encodeHexString(byHMAC).toUpperCase();

                                System.out.println("Encrypted data =\n "+stEncryptedData);
                        }
                }
                catch(Exception e)
                {
                        System.out.println("Exception in the file reading"+e);
                }

        }

        private static byte[] encode(byte[] hexkey, byte[] hexvalue) {
                try {
                        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                        SecretKeySpec secret_key = new SecretKeySpec(hexkey, "HmacSHA256");
                        sha256_HMAC.init(secret_key);

                        return sha256_HMAC.doFinal(hexvalue);

                } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                } catch (InvalidKeyException e) {
                        e.printStackTrace();
                }

                return null;
        }

        public static String stringToHex(String base) throws UnsupportedEncodingException
        {
                return String.format("%040x", new BigInteger(1, base.getBytes(StandardCharsets.US_ASCII)));
        }

        public static byte[] stringToHexByte(String base) throws UnsupportedEncodingException
        {
                System.out.println(stringToHex(base).toUpperCase());
                return DatatypeConverter.parseHexBinary(stringToHex(base).toUpperCase());
        }

}
