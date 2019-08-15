import java.io.IOException;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Encryption {
public static final int a = 0x9F224;
public static final int b = 0x98C36;
public static final int c = 0x776a2;
public static final int d = 0x87667;

private String preMaster;
IvParameterSpec ivSpec = new IvParameterSpec(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x01 });
private byte[] text;
private SecretKey secret;
private byte[] sKey;
protected SecretKey passwordKey;
protected PBEParameterSpec paramSpec;
public static final String ENCYT_ALGORITHM = "AES/CBC/PKCS7Padding";
public static final String KEY_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC" ;
//BENCYT_ALGORITHMSE64Encoder encod = new BENCYT_ALGORITHMSE64Encoder();
//BENCYT_ALGORITHMSE64Decoder decod = new BENCYT_ALGORITHMSE64Decoder();

public Encryption(String preMaster,String text,int x){      
    this.preMaster=preMaster;       
    this.text=Encoder.decode(text.toCharArray());
    try {

        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        secret = kg.generateKey();
    } catch (Exception e) {
        // TODO ENCYT_ALGORITHMuto-generated catch block
        e.printStackTrace();
    }
}

public Encryption(String key,String text){
    try {
        this.text = Encoder.decode(text.toCharArray());
        this.sKey = Encoder.decode(key.toCharArray());
    } catch (Exception e) {
        e.printStackTrace();
    }

}   

public String preMaster() {

    byte[] keys = null;
    keys = preMaster.getBytes();
    int x = -1;
    int process = 0;
    while (x < keys.length - 2) {
        x++;
        switch (x) {
        case 1:
            process = keys[x + 1] | a ^ c & (d | keys[x] % a);
        case 2:
            process += a | (keys[x] ^ c) & d;
        case 3:
            process += keys[x] ^ (keys[x + 1] / a) % d ^ b;
        default:
            process += keys[x + 1] / (keys[x] ^ c | d);
        }
    }

    byte[] xs = new byte[] { (byte) (process >>> 24),
            (byte) (process >> 16 & 0xff), (byte) (process >> 8 & 0xff),
            (byte) (process & 0xff) };
    preMaster = new String(xs);
    KeyGenerators key = new KeyGenerators(preMaster);
    String toMaster = key.calculateSecurityHash("MD5")
            + key.calculateSecurityHash("MD2")
            + key.calculateSecurityHash("SHA-512");


    return toMaster;
}

public String keyWrapper(){
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
    Key SharedKey = secret;
    String key = null;
    char[] preMaster = this.preMaster().toCharArray();
    try {

        byte[]salt={ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06}; 
        paramSpec = new PBEParameterSpec(salt,20);
        PBEKeySpec keySpec = new PBEKeySpec(preMaster);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        passwordKey = factory.generateSecret(keySpec);
        Cipher c = Cipher.getInstance(KEY_ALGORITHM);
        c.init(Cipher.WRAP_MODE, passwordKey, paramSpec);
        byte[] wrappedKey = c.wrap(SharedKey);
        key=Encoder.encode(wrappedKey);
    }catch(Exception e){
        e.printStackTrace();
    }
    return key;
}

public Key KeyUnwrapper(){
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
    byte[] wrappedKey = sKey;
    Key unWrapped = null;
    try{
        Cipher c = Cipher.getInstance(KEY_ALGORITHM,"BC");
        c.init(Cipher.UNWRAP_MODE, passwordKey, paramSpec);
        unWrapped = c.unwrap(wrappedKey, ENCYT_ALGORITHM, Cipher.SECRET_KEY);

    }catch(Exception e){
        e.printStackTrace();
    }
    return unWrapped;
}
public String encrypt(){
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 

    SecretKey key = secret;
    String result=null;
    try{
        Cipher cipher = Cipher.getInstance(ENCYT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        result =Encoder.encode(cipher.doFinal(text));
    }catch(Exception e){
        e.printStackTrace();
    }
    return result;
}



public String decrypt(){
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
    String result = null;
    SecretKey key = (SecretKey) KeyUnwrapper();
    try{
        Cipher cipher = Cipher.getInstance(ENCYT_ALGORITHM, "BC");
        cipher.init(Cipher.DECRYPT_MODE, key);
        result =  Encoder.encode(cipher.doFinal(text));
    }catch(Exception e){
        e.printStackTrace();
    }
    return result;
}

public static void main(String[] args) throws IOException{
    Encryption en = new Encryption("123456","Hello World",0);
    String enText = en.encrypt();
    String key = en.keyWrapper();
    System.out.println(key);
    System.out.println(enText);
    Encryption de = new Encryption(key,enText);
    String plainText = de.decrypt();
    System.out.println(plainText);
}
