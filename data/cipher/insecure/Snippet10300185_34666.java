public class CryptoLibrary {

private Cipher encryptCipher;
private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

public CryptoLibrary() throws SecurityException{

    java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());

    char[] pass = "NNSHHETJKKSNKH".toCharArray();
    byte[] salt = {
    (byte) 0xa3, (byte) 0x21, (byte) 0x24, (byte) 0x2c,
    (byte) 0xf2, (byte) 0xd2, (byte) 0x3e, (byte) 0x19 };

    init(pass, salt, iterations);

}

public void init(char[] pass, byte[] salt, int iterations)throws SecurityException{

        PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));

        encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, k, ps);
    }
}

public synchronized String encrypt(String str)  throws SecurityException{
    if(str!=null){
        byte[] utf8 = str.getBytes("UTF8");
        byte[] enc = encryptCipher.doFinal(utf8);
        return encoder.encode(enc);
    }
    else {
        return null;
    }
}
}
