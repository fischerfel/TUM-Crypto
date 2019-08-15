byte[] data;
String key = "tkg96827pco74510";

byte[] encryptedOut;
String decryptedOut;

Key aesKey;
Cipher cipher;

public void setData(String dataIn){
    this.data = dataIn.getBytes();
    try {
        aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");

    }catch(Exception e){
        System.out.println("SET DATA ERROR - " + e);
    }
}
public void encrypt() {
    try{
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        encryptedOut = cipher.doFinal(data);
    }catch(Exception e){
        System.out.println(e);
    }
}

public void decrypt(){
    try {
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        decryptedOut = new String(cipher.doFinal(data));

    }catch(Exception e){
        System.out.println("Decrypt Error: " + e);
    }
}

public byte[] getEncrypted() {
   return encryptedOut;
}

public String getDecrypted(){
    return decryptedOut;
}
