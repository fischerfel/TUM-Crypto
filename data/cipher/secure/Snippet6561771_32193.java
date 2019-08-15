public static final String ENCYT_ALGORITHM = "AES/ECB/PKCS7Padding";
public static final String KEY_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC" ;
//BENCYT_ALGORITHMSE64Encoder encod = new BENCYT_ALGORITHMSE64Encoder();
//BENCYT_ALGORITHMSE64Decoder decod = new BENCYT_ALGORITHMSE64Decoder();

public Encryption(String preMaster,String text,int x){      
    this.preMaster=preMaster;       
    this.text=text.getBytes();
}
public void keyGenerator(){
    KeyGenerator kg = null;
    try {
        kg = KeyGenerator.getInstance("AES");
        secret = kg.generateKey();
    } catch (Exception e) {
        // TODO ENCYT_ALGORITHMuto-generated catch block
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
        paramSpec = new PBEParameterSpec(salt,256);
        PBEKeySpec keySpec = new PBEKeySpec(preMaster,salt,1024,256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        passwordKey = factory.generateSecret(keySpec);
        Cipher c = Cipher.getInstance(KEY_ALGORITHM);
        c.init(Cipher.WRAP_MODE, passwordKey, paramSpec);
        byte[] wrappedKey = c.wrap(SharedKey);
        key=new String(wrappedKey,"UTF8");
    }catch(Exception e){
        e.printStackTrace();
    }
    return key;
}
