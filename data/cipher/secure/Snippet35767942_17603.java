import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class DJExampleDecryptCode
{

  public static void main(String[] args)
  {

    try
    {
      String encryptedText = "q3sEN1NZZyseoFy9H3WIwNf9jpGrDTTqh/AticRV2pnb1KZ5lieuK5jw3JgctgYUnTE03xnMcOL50UGKZ4dbYEt5XGCZyNVgh1qVGF7Vgnvi5PKndnpKLcoSUJCcbu/lyLI2P+Zd7ZH0/tRKRn1zqrPAWUH3VjtUt7qkIcdIYyaoHP5I7eiZRk6FL9ugUQJnz8WFgM4mcRJ5Zs/NLdaXKeHMO4nPQBTOLNaPdNxW2MM+qlv0HN/fs4rPMRGUw0xXhjWsmSNqadASfn7UX4fL79CmGyKfm8ol4njZakZbsfes/zstc5Su0swycfFSkjXAjPjvMGdBs5/HSLXYAvQPoA==";

      String TextToEncrypt = "Test";

      String decryptedString = decrypt(encryptedText);
      String encryptedString = encrypt(TextToEncrypt);

      //Printing
      System.out.println("Decrypted Text: " + decryptedString);
      System.out.println("Encrypted Text: " + encryptedString);

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  //public static String decrypt(String text, String privateKeyString) throws GeneralSecurityException
  public static String decrypt(String text) throws GeneralSecurityException
  {
    String DJPrivateKey = ".....";  
    PrivateKey privateKey = stringToPrivateKey(DJPrivateKey);

    byte[] dectyptedText = null;
    try
    {
      final Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      dectyptedText = cipher.doFinal(base64Decode(text));

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return new String(dectyptedText);
  }

  public static String encrypt(String data) throws GeneralSecurityException 
  {
      String PCMSPublicKey = "MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIV/0v/U5+6pgCAggAMBQGCCqGSIb3DQMHBAhSuqMZCoVAxgSCBMhKaVDCjOS9q0jEc+nUCEKQD73pHte/9nMdTkHbFGpu+1amzpY0YLhgqVTQhDp43amH6IvM3KfmM+9M5i0bXBksa+5la2kVl8Ntw6fzc1xFtcSMLb8CFkk0gV3l6kcKEo1rN2TiH3jGQz43DFHUJbnITWwY3SFCsWPZF2oegTAMSEKhOJ6h9bad3KoqNmqji6hdk9ONhQBarDrZGL9l+8GWnx9TyVxAVltBxzv0DRlqXlKhVkfV2XqkiECcilFHeoaI+cV6W1z8S9kFPdnm93QjCu88l1lG1a5ox4tu4dPHj4u7uVZKuEBkpr7HpF0uL+o+JEflNjUl/BYlS3++l6lfRpOp6mb2VVt1zQgoKVR1wZZeSoEqhJ/r75Krybq4TdXXqs4IdPZmSwIPTVM8n5ZvzUz/F+K46rYIQchz3GPCpKEPI/9+OhqUKtXe2KpPsZtD7hJ7+r1g99MwzEjyET6l7lLuIE2SpKS0wxZB5qb0+92+SfyPwQWIM5tfv1Gs7M8A2MFz9GclXsaGpt+mx0DQPiMkpdoB9e5GbO1PGlP3MWhUmQ6wwIUVVjGryJuvLFL+4psDeUzkdKilG9nPDVSWHLlCx1C3k8hcuJUy1bTihrFprcOEjuzGzmhp3IUQc+5Z4dydM/2AmYFCNmjySRKYZVRBPfPrcVfDo8P1lzlYeXLcWubMlwxyRfv/WjzJkyMlDSiYEEnkcYJrCgeocsU8qJ+yq1QAsTs361Svi2tQ7lJZjp1FtIdvMr+U32eW1Pri+vn4LWdPcPzxbrLHN/daV+l3Ttw493x0R7WMDnhcS8yhd8NlWoh43IzmQDCn33Lek7WS3HmSzTAg6bfxmYZf9Ogn86DR/q2c2ZKwbs3nloHdfkKklOOqgRPic7nP8khsd+pjTULZUDmKa3d0OW8Ps5fTY/GzWrJBLVEoM8bN0w3CUsHixSQOh1pMwJUiyAT/cJPZfru1gtUeNkSJ5u/atc6HaPc6sfrhLF6RWyhIYKyqoM2dyLFC3hi+N0ZLBZwp/tnIou24dtwlJnLvKCinzO0pUTJC2yOwsnfL57h+ikdd3xS9fMWwpiSdNps086japrp12GU9VyBZX8b9QEip/Hxw778OK1x853+WYM978wNPrFwIfWtQpvNZMi8Mt0WXDvfHiG8JK9PKDoS25iV8SrwZScfBTMIi95j419BuhcVca1fi0keEEKaqMzBus4Mgz421Qcy0xv2u81w90qoMyXBwadRODtrJBQIovHBCKVRkxEm64Rr3fCWGjralnKcjxDKa77qakFlFOyNJsplnlC0mc229E19JXlxpcdDlivscE727KLLYu8rPUEWMZY/PA0D9JH+u+52A81ur0vuCnTLF1V0WG8ECJwgVTbIfByPoi+MKuvmW1pvixwBXiIh7as95gVl47HAXmCYOj0bnD0yO/pFSLoiAURl0j2R/c/NtKjz5TQ9F3O3U83UojatwtIyc7xN6Bs+iwTPaBOJrI6Sbgc5yDJPFczhPQDSpFZVtTUSeq1UA7N7mogwQLAqawGBHQJ9JIapMl6uC7Y2nN9O4lYRtbQLBKpZ3xzIk9LrDyT3F/w+c3l/lVUz0X6lz746zNutl/6f6XKI5oeis7/b5rMHHYYE=";
      PublicKey publicKey = stringToPublicKey(PCMSPublicKey);


      byte[] enctyptedText = null;
        try
        {
          final Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.ENCRYPT_MODE, publicKey);
          enctyptedText = cipher.doFinal(base64Decode(data));

        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }

        return new String(enctyptedText);

  }    

  public static byte[] base64Decode(String encodedString)
  {
    Base64 myBase64 = new Base64();

    return myBase64.decode(encodedString);
  }



  public static PrivateKey stringToPrivateKey(String privateKeyString) throws GeneralSecurityException
  {
    byte[] clear = base64Decode(privateKeyString);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey priv = fact.generatePrivate(keySpec);
    Arrays.fill(clear, (byte) 0);
    return priv;
  }

  public static PublicKey stringToPublicKey(String publicKeyString) throws GeneralSecurityException
  {

      byte[] clear = base64Decode(publicKeyString);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(clear);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PublicKey pub = fact.generatePublic(keySpec);
    Arrays.fill(clear, (byte) 0);
    return pub;
  }

}
