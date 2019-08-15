public class RSAcipher {

private static final String TAG = "RSA Cipher";

private SharedPreferences keyDat=null;
private SharedPreferences.Editor keyEdit=null;
private String
        pubKeyStr=null,
        priKeyStr=null;
private Key
        publicKey=null,
        privateKey=null;

private static Context ct=null;
private static RSAcipher savedInstance=null;

public static RSAcipher getInstance(Context context) {
    ct = context;
    if(savedInstance==null){
        savedInstance = new RSAcipher();
    }
    return savedInstance;
}

private RSAcipher() {
    keyDat = PreferenceManager.getDefaultSharedPreferences(ct);
    keyEdit = keyDat.edit();
    pubKeyStr = keyDat.getString("spPubKeyTag",null);
    priKeyStr = keyDat.getString("spPriKeyTag",null);
    if(pubKeyStr==null && priKeyStr==null){
        generateKeys();
    }
}

private void generateKeys(){
    try{
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(1024, new SecureRandom());
        KeyPair keyPair = kpg.genKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        byte[] pubByte = publicKey.getEncoded();
        pubKeyStr = new String(Base64.encodeToString(pubByte,Base64.DEFAULT));
        byte[] priByte = privateKey.getEncoded();
        priKeyStr = new String(Base64.encodeToString(priByte,Base64.DEFAULT));

        keyEdit.putString("spPubKeyTag",pubKeyStr);
        keyEdit.putString("spPriKeyTag",priKeyStr);
        keyEdit.commit();
    } catch (Exception e){
        Log.e(TAG,"key generation error");
    }
}

private Key getPubKey(){
    String pubKeyStr = keyDat.getString("spPubKeyTag", "");
    byte[] sigBytes = Base64.decode(pubKeyStr, Base64.DEFAULT);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
    KeyFactory keyFact = null;
    try {
        keyFact = KeyFactory.getInstance("RSA", "BC");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        e.printStackTrace();
    }
    try {
        return  keyFact.generatePublic(x509KeySpec);
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }
    return null;
}

private  Key getPriKey(){
    String privKeyStr = keyDat.getString("spPriKeyTag", "");
    byte[] sigBytes = Base64.decode(privKeyStr, Base64.DEFAULT);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
    KeyFactory keyFact = null;
    try {
        keyFact = KeyFactory.getInstance("RSA", "BC");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        e.printStackTrace();
    }
    try {
        return  keyFact.generatePrivate(x509KeySpec);
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }
    return null;
}

public String getEncoded(String str){
    if(publicKey==null){
        publicKey = getPubKey();
    }
    String enstr = new String();
    byte[] encodedBytes = null;
    try{
        Cipher c = Cipher.getInstance("RSA", "BC");
        c.init(Cipher.ENCRYPT_MODE,publicKey);
        encodedBytes = c.doFinal(str.getBytes());
    } catch (Exception e){
        Log.e(TAG, "RSA Encryption error");
    }
    enstr = Base64.encodeToString(encodedBytes,Base64.DEFAULT);
    return enstr;
}

public  String getDecoded(String str){
    if(privateKey==null){
        privateKey = getPriKey();
    }
    String destr = new String();
    byte[] encodedBytes = Base64.decode(str,Base64.DEFAULT);
    byte[] decodedBytes = null;
    try{
        Cipher c = Cipher.getInstance("RSA", "BC");
        c.init(Cipher.DECRYPT_MODE,privateKey);
        decodedBytes = c.doFinal(encodedBytes);
    }catch(Exception e){
        Toast.makeText(ct, e.toString(), Toast.LENGTH_LONG).show();
    }
    try{destr = new String(decodedBytes);}catch (Exception e){}
    return destr;
}}
