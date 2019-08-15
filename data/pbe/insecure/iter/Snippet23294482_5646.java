public class Cryption {
private static int KEYLEN_BITS = 256;
private static int ITERATIONS = 128;

static private  Log log = LogFactory.getLog(Cryption.class);    

private static SecretKeyFactory secretKeyFactory = null;
private static  Map<String, SecretKey> secretKeyMap = new HashMap<String, SecretKey>(); 

private String password = null;
private SecretKey secretKey = null;
private Cipher cipher = null;

private synchronized static SecretKeyFactory createSecretKeyFactory() {
    try {
        if(secretKeyFactory == null) {                       
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");          
        }       
    } catch(Exception e) { 
        log.error("createSecretKeyFactory", e);
    }
    return secretKeyFactory;
}

private static SecretKey createSecretKey(String password, byte []salt) {
    SecretKey secretKey = secretKeyMap.get(password);
    if(secretKey == null) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEYLEN_BITS);
            SecretKey tmpSecretKey = createSecretKeyFactory().generateSecret(spec);
            secretKey = new SecretKeySpec(tmpSecretKey.getEncoded(), "AES");
            secretKeyMap.put(password, secretKey);
        } catch(Exception e) { 
            log.error("getSecretKey", e);
        }
    }
    return secretKey;
}

private boolean createCipher(String password, byte []salt, byte[] iv, int mode) {
    try {
        this.password = password;
        secretKey = createSecretKey(password, salt);    
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        if(iv != null) {
            cipher.init(mode, secretKey, new IvParameterSpec(iv));
        } else {
            cipher.init(mode, secretKey);               
        }
    } catch(Exception e) { 
        log.error("getCipher", e);
        return false;
    }
    return true;
}

public String getPassword() {
    return password;
}

public byte[] getCipherIV() {
    byte [] iv = null;

    try {       
        AlgorithmParameters params = cipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    } catch(Exception e) {
        log.error("getCipherIV", e);
    }
    return iv;
}

public byte[] execute(byte []in) {
    byte [] out = null;

    try {       
        out = cipher.doFinal(in); 
    } catch(Exception e) { 
        log.error("execute", e);
    }
    return out;
}

static Cryption create(String password, byte[] salt, byte[] iv, int mode) {
    Cryption cryption = new Cryption();

    cryption.createCipher(password, salt, iv, mode);
    return cryption;
}

static void init(Cryption cryption, byte[] iv, int mode) {
    try {       
        if(iv != null) {
            cryption.cipher.init(mode, cryption.secretKey, new IvParameterSpec(iv));
        } else {
            cryption.cipher.init(mode, cryption.secretKey);             
        }
    } catch(Exception e) { 
        log.error("init", e);
    }
}   
}
