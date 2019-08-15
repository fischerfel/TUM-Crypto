import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;



class AESCrypt {


// encrypt
def encrypt (def plainText, def secret) {

def cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")


// converty string secret to SecretKeySpec
byte[] decodedKey = Base64.getDecoder().decode(secret);
SecretKeySpec key= new SecretKeySpec(decodedKey , 0, decodedKey.length, 
"AES");


cipher.init(Cipher.ENCRYPT_MODE, key)

return cipher.doFinal(plainText.getBytes("UTF-8")).encodeBase64().toString()  

}

}

//Main
for( int i = 0; i < dataContext.getDataCount(); i++ ) {
  InputStream is = dataContext.getStream(i);
  Properties props = dataContext.getProperties(i);

def c = new AESCrypt()

def secret = 
"B3FFCA612CD0C3D9050A4DE3588E2830F26BEF6D7E1CEC77DD2F22FAFC038D33"

//get plaintext of payload
Scanner s = new Scanner(is).useDelimiter("\\A");
String plainPayload = s.hasNext() ? s.next() : "";

//encrypt plaintext of payload
def encryptedPayload = c.encrypt(plainPayload, secret)

println encryptedPayload + "\n"



}
