private static final String KEY = "a2[..]";
private static final String SALT = "t[..]";
private static final String IV = "u[..]";
private static final String DES_EDE_PKCS5 = "DESede/CBC/PKCS5Padding"

public static String encrypt(String password) {
    byte[] byteSalt = Base64.decode(SALT, Base64.DEFAULT);
    byte[] bytesIv = Base64.decode(IV, Base64.DEFAULT);
    String mdp = "";        
    try {           
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");          
        KeySpec spec = new PBEKeySpec(KEY.toCharArray(), byteSalt, NB_ITER_RFC, SIZE_KEY);          
        SecretKey secretKey = factory.generateSecret(spec);             
        Cipher c = Cipher.getInstance(DES_EDE_PKCS5);           
        IvParameterSpec ivParam = new IvParameterSpec(bytesIv);         
        c.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);                
        byte[] encrypted = c.doFinal(password.getBytes("UTF-8"));
        mdp = Base64.encodeToString(encrypted, Base64.DEFAULT);                 
    }
    catch [..]
    return mdp;
}
