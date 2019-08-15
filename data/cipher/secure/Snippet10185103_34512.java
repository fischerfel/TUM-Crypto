  import java.security.Key;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.security.Security;
    import java.util.Arrays;
    import javax.crypto.Cipher;
    import javax.crypto.KeyGenerator;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;
    import java.security.MessageDigest;

 public class SHAhashingexample 
    {
        public static void main(String[] args)throws Exception
        {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            String usernametohash = "123456";
            String salt="salty food";
            String userdata="hello how are you!";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((usernametohash+salt).getBytes());
            byte byteData[] = md.digest();
            System.out.println("Digested value : " + byteData);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) 
            {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
             System.out.println("Hash code value of username : " + sb);
               byte[] actualkey=sb.toString().getBytes();
             byte[] first_key = Arrays.copyOf(actualkey, 16);
            Key key = new SecretKeySpec(first_key, "AES");
            System.out.println("Derived AES key is "+key.toString());
            SecureRandom random = new SecureRandom();
            IvParameterSpec ivSpec = createCtrIvForAES(1, random);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] cipherText = cipher.doFinal(userdata.getBytes()); 
            System.out.println("ciphertext is : " + cipherText);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] plainText = cipher.doFinal(cipherText)  ; 
            System.out.println("plaintext is : " + plainText);

          }

        public static IvParameterSpec createCtrIvForAES(int messageNumber, SecureRandom random)
        {
            byte[] ivBytes = new byte[16];
            random.nextBytes(ivBytes);
           for (int i = 0; i < 16; i++)
           {
              ivBytes[i] = 0;
            }
          return new IvParameterSpec(ivBytes);
          }
    }
