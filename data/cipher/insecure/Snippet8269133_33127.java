import javax.crypto.Cipher;  
import javax.crypto.spec.SecretKeySpec;  

public class AESTest {  
     public static String asHex (byte buf[]) {
          StringBuffer strbuf = new StringBuffer(buf.length * 2);
          int i;

          for (i = 0; i < buf.length; i++) {
           if (((int) buf[i] & 0xff) < 0x10)
            strbuf.append("0");

           strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
          }

          return strbuf.toString();
     }

     public static void main(String[] args) throws Exception {  
        String keyString = "ssssssssssssssss";  
        // 546578746F2070617261207465737465 (Hex)  
        byte[] key = keyString.getBytes();  
        System.out.println(asHex(key).toUpperCase());  

        String clearText = "sdhhgfffhamayaqqqaaaa";  
        // ZXNzYXNlbmhhZWhmcmFjYQ== (Base64)  
        // 6573736173656E686165686672616361 (Hex)  
        byte[] clear = clearText.getBytes();  
        System.out.println(asHex(clear).toUpperCase());  

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");  
        // PKCS5Padding or NoPadding  
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");  
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  

        byte[] encrypted = cipher.doFinal(clear);  
        System.out.println(asHex(encrypted).toUpperCase());  
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original =
                    cipher.doFinal(encrypted);

        System.out.println(original);
        String originalString = new String(original);
        System.out.println("Original string: " +
                    originalString + " " + asHex(original));
    }  
}  
