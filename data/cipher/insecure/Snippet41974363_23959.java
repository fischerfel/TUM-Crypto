package mundo;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

class AESTest {
   public static void main(String[] args) throws Exception {
     //each array is a vector case {key, plainText, expectedCipher}
     String[][] cases = new String[][]{{"00000000000000000000000000000000", "f34481ec3cc627bacd5dc3fb08f273e6","0336763e966d92595a567cc9ce537f5e"},
                                       {"00000000000000000000000000000000", "9798c4640bad75c7c3227db910174e72", "a9a1631bf4996954ebc093957b234589"},
                                       {"2b7e151628aed2a6abf7158809cf4f3c", "6bc1bee22e409f96e93d7e117393172a", "3ad77bb40d7a3660a89ecaf32466ef97"},
                                       {"2b7e151628aed2a6abf7158809cf4f3c", "ae2d8a571e03ac9c9eb76fac45af8e51", "f5d3d58503b9699de785895a96fdbaaf"}};
     for(String[] kase : cases)
     {
         byte[] theKey = byte2hex(kase[0]);
         byte[] theMsg = byte2hex(kase[1]);
         byte[] theExp = byte2hex(kase[2]);
         Cipher cipher = Cipher.getInstance("AES");
         SecretKeySpec keySpec = new SecretKeySpec(theKey, "AES");
         cipher.init(Cipher.ENCRYPT_MODE, keySpec);
         byte[] cryptMsg = cipher.doFinal(theMsg);
         System.out.println("Key     : "+hex2byte(theKey));
         System.out.println("Message : "+hex2byte(theMsg));
         System.out.println("Cipher  : "+hex2byte(cryptMsg));
         System.out.println("Expected: "+hex2byte(theExp) + "\n");
     }
   }
   public static String hex2byte(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
   }
   public static byte[] byte2hex(String s) {
        return DatatypeConverter.parseHexBinary(s);
   }
}
