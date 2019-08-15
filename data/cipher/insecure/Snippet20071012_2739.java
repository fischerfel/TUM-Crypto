import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.io.*;
import java.lang.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


    public class TRIPPLE_DES {


        public static void main(String[] args) throws Exception {


    String text = "cardNumber=28293939493330&securityCode=123&cardExpiryMonth=07&cardExpiryYear=2013&cardHolderName=Test&transactionAmount=50.00&currencyCode=356&customerReferenceNo=9393938393938&cardProvider=VISA&name=Test&mobileNo=983345123412&email=test@test.com&contactNo=983345123412&password=wyzgames&amount=100&remoteIP=10.10.10.50&checkSum=92be84d25b60b3f9f233c074d12ade1ddef158cb369a0734afff3fb6adc9d7ddb4b26f7e6001563747a8d47457e713750e5802b4871cfbe70baca9304d4c385f";


         String codedtext = new TRIPPLE_DES().encrypt(text);
         String decodedtext = new TRIPPLE_DES().decrypt(codedtext);


         String encodedurl = URLEncoder.encode(codedtext,"UTF-8");
         System.out.println(encodedurl);
         System.out.println(decodedtext);
        }


        public String encrypt(String message) throws Exception {
         final MessageDigest md = MessageDigest.getInstance("md5");
         final byte[] digestOfPassword = md.digest("HG58YZ3CR9".getBytes("utf-8"));
         final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);


         for (int j = 0, k = 16; j < 8;) {
          keyBytes[k++] = keyBytes[j++];
         }


         final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
         final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
         final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, key, iv);


         final byte[] plainTextBytes = message.getBytes("utf-8");
         final byte[] cipherText = cipher.doFinal(plainTextBytes);

         return Base64.encodeBase64String(cipherText);
        }


        public String decrypt(String message) throws Exception
        {
         final MessageDigest md = MessageDigest.getInstance("md5");
         final byte[] digestOfPassword = md.digest("HG58YZ3CR9".getBytes("utf-8"));
         final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
         for (int j = 0, k = 16; j < 8;) {
          keyBytes[k++] = keyBytes[j++];
         }


         final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
         final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
         final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
         decipher.init(Cipher.DECRYPT_MODE, key, iv);



         byte[] byteMessage= Base64.decodeBase64(message);
         final byte[] plainText = decipher.doFinal(byteMessage);


         return new String(plainText, "UTF-8");
        }
    }
