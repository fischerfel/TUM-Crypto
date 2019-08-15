public class SampleEncryptor extends SecretKeyWrapper{
private static final SecureRandom RANDOM = new SecureRandom();

private SharedPreferences pref;
private SecretKey secretKey;
private byte[] encryptedKey;

public SampleEncryptor(Context context)
        throws GeneralSecurityException, IOException {
    super(context, "myalias");
    this.pref = context.getSharedPreferences("mySecretKeyStore", Context.MODE_PRIVATE);
    String base64key = pref.getString("key", null);
    if(base64key==null){
        this.secretKey = KeyGenerator.getInstance("AES").generateKey();
        this.encryptedKey = this.wrap(this.secretKey);
        pref.edit().putString("key", Base64.encodeToString(encryptedKey, Base64.DEFAULT)).commit();
    }else{
        this.encryptedKey = Base64.decode(base64key, Base64.DEFAULT);
        this.secretKey = this.unwrap(this.encryptedKey);
    }
}

public byte[] encrypt(byte[] data) throws GeneralSecurityException{     
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    return  cipher.doFinal(data);
}

public byte[] decrypt(byte[] data) throws GeneralSecurityException{
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return  cipher.doFinal(data);
}

public String encryptBoolean(boolean value) throws GeneralSecurityException{
    BigInteger i = new BigInteger(128, RANDOM);
    if(value){
        i = i.setBit(0);
    }else{
        i = i.clearBit(0);
    }
    byte[] encrypted =  encrypt(i.toByteArray());

    return Base64.encodeToString(encrypted, Base64.DEFAULT);
}

public Boolean decryptBoolean(String encrypted) throws GeneralSecurityException{
    byte[] data = Base64.decode(encrypted, Base64.DEFAULT);     
    BigInteger i = new BigInteger(decrypt(data));
    return i.testBit(0);
}
