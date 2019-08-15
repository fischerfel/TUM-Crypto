// Constants
private final static String encoding = "UTF-8";
private final static String algorithm = "AES/CBC/PKCS5Padding";
private final static int NCHARS = 16;

// Private variables
private byte[] rawdata;
private byte[] keyBytes;
private String status;
private String fileName;

// AES Variables
private static Cipher cipher;
private static SecretKeySpec secretKeySpec;
private static IvParameterSpec ivParameterSpec;

//====================== AES Engine intialization ======================
public AESCrypt (String passwd,String datafile){

    // Initializing variables.
    rawdata = null;
    status = "";
    fileName = datafile;

    // Transforming the passwd to 16 bytes.
    try {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        InputStream in = new ByteArrayInputStream(Charset.forName(encoding).encode(passwd).array());
        byte[] buffer = new byte[NCHARS];
        int byteCount;
        while ((byteCount = in.read(buffer)) > 0) {
            digester.update(buffer, 0, byteCount);
        }
        keyBytes = digester.digest();
    }
    catch(Exception e){
        status = "Error in key generation: " + e.toString();
    }

    // Initilizing the crypto engine
    try {
        cipher = Cipher.getInstance(algorithm);
    }
    catch(Exception e){
        status = "Error in intialization: " + e.toString();
    }
    secretKeySpec = new SecretKeySpec(keyBytes, "AES");
    ivParameterSpec = new IvParameterSpec(keyBytes);

}
