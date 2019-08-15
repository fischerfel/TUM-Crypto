public void initCiphers(char password[]) {

PBEKeySpec pbeKeySpec;
PBEParameterSpec pbeParamSpec;
SecretKeyFactory keyFac;

byte[] salt = {
   (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
   (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
};
int count = 20;
pbeParamSpec = new PBEParameterSpec(salt, count);          
pbeKeySpec = new PBEKeySpec(password);
try {
    keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
    encryptCipher = Cipher.getInstance("PBEWithMD5AndDES");
    decryptCipher = Cipher.getInstance("PBEWithMD5AndDES");    
    encryptCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
    decryptCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);       
} catch (Exception e) { 
    Log.v("tag", e.toString()); 
}
