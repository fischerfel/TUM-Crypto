import java.security.PublicKey; 
import java.security.Security; 
import javax.crypto.Cipher; 

public class DecyrptTest { 
    public static void main (String[] args) throws Exception { 
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
        //msg is some secret msg ,i have deleted. 
        String msg = ""; 
        byte[] signedMsgBytes = SHA1.sign(msg); 
        String signedMessageOther=StringTools.bytesToHex(signedMsgBytes); 
        System.out.println("signedMessageOther: "+signedMessageOther); 

        String ct="1386cfed01490b9026903722324f80f8a56cc38169b46e15154ce9e7168ff589282855002e195a0c1a96d5fe540a7fa97b01ae24f365f39302e0c1186ee9308d6b94526741f7093dc2678c713bb2b1a8a6942decb35b16725353da523417cb835cea903485b19b63c2c444c8bc6c865ea78c749f10ca70b266f6078192f5c76c"; 

        Cipher cipher = Cipher.getInstance("RSA", "BC"); 
        PublicKey pubKey = KeyUtil.getPubKeyFromFile("res/key.pub"); 
        System.out.println("Algorithm: "+pubKey.getAlgorithm()); 
        System.out.println("Format: "+pubKey.getFormat()); 
        System.out.println("Key Length: "+pubKey.getEncoded().length); 

        cipher.init(Cipher.DECRYPT_MODE, pubKey); 
        byte[] ctBytes=StringTools.hexStringToByteArray(ct); 
        System.out.println(ctBytes.length); 
        byte[] cipherText2 = cipher.doFinal(ctBytes); 
        System.out.println("cipher: " + StringTools.bytesToHex(cipherText2)); 
    } 
}
