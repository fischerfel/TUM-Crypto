public class AES256
{
private String charSet = "UTF-8";
private String algo = "AES/CBC/PKCS5Padding";
private String baseAlgo = "AES";
private String hashAlgo = "PBKDF2WithHmacSHA1";

private String key = null;
private String salt = "defaultsaltsalt";
private String iv = "a1bC@6jZ!#sL1z0y";

private Cipher cipher;
private BufferedInputStream bIs;
private BufferedOutputStream bOs;

public AES256()
{

}

public AES256(String pass)
{
    this.key = pass;
}

public AES256(String pass, String salty)
{
    this.key = pass;
    this.salt = salty;
}

public AES256(String pass, String salty, String ivs)
{
    this.key = pass;
    this.salt = salty;
    this.iv = ivs;
}

public void setKey(String key)
{
    this.key = key;
}

public void setSalt(String salt)
{
    this.salt = salt;
}

public void setIV(String ivs)
{
    this.iv = ivs;
}

/**
 * @Method Pads and constructs the SecretKey (Padding @ 32)
 * @return Returns the padded key.
 * @throws Exception Exception is thrown if the key is null or something else wrong..
 */
public SecretKeySpec getKey() throws Exception
{
     byte[] saltBytes = salt.getBytes(charSet);

     SecretKeyFactory factory = SecretKeyFactory.getInstance(hashAlgo);
     PBEKeySpec spec = new PBEKeySpec(
             this.key.toCharArray(), 
             saltBytes, 
             300000, //make variable
             263 //default 32 bytes
     );

     SecretKey secretKey = factory.generateSecret(spec);
     SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), baseAlgo);

     return secret;
}

/**
 * @Method Pads and returns the IV (Padding @ 16)
 * @return
 * @throws Exception
 */
public byte[] getIV() throws Exception
{
    byte[] byteKey = iv.getBytes(charSet);
    MessageDigest sha = MessageDigest.getInstance("SHA-512");
    byteKey = sha.digest(byteKey);
    byteKey = Arrays.copyOf(byteKey, 16);
    return byteKey;
}

public byte[] encrypt(byte[] plainText) throws Exception
{
    cipher = Cipher.getInstance(algo);
    cipher.init(Cipher.ENCRYPT_MODE, getKey(), new IvParameterSpec(getIV()));

    System.out.println("Plain text length: "+plainText.length);
    byte[] enc = Base64.encodeBase64(cipher.doFinal(plainText));
    System.out.println("Encrypted text length "+enc.length);

    return  enc;
}

public byte[] decrypt(byte[] encryptedText) throws Exception
{
    cipher = Cipher.getInstance(algo);
    cipher.init(Cipher.DECRYPT_MODE, getKey(), new IvParameterSpec(getIV()));

    System.out.println("Encrypted Decrypted Text length: "+encryptedText.length);
    byte[] de = cipher.doFinal(Base64.decodeBase64(encryptedText));
    System.out.println("Decrypted Text length: "+de.length);

    return de;
}

public void encrypt(File fileToEncrypt) throws FileNotFoundException, IOException, Exception
{
    if(fileToEncrypt == null)
        throw new FileNotFoundException("File given to encrypt was not found!");
    File encrypted = new File(cutPath(fileToEncrypt.getPath()), "ENCRYPTED "+fileToEncrypt.getName());
    if(!encrypted.exists())
        encrypted.createNewFile();
    bIs = new BufferedInputStream(new FileInputStream(fileToEncrypt));
    bOs = new BufferedOutputStream(new FileOutputStream(encrypted));

    @SuppressWarnings("unused")
    int read = 0;
    byte[] buff = new byte[1024];
    while((read = bIs.read(buff)) != -1)
    {
        byte[] enc = encrypt(buff);
        bOs.write(enc, 0, enc.length);
    }
    bIs.close();
    bOs.close();
}

public void decrypt(File fileToDecrypt) throws FileNotFoundException, IOException, Exception
{
    if(fileToDecrypt == null)
        throw new FileNotFoundException("File given to decrypt was not found!");
    File decrypted = new File(cutPath(fileToDecrypt.getPath()), "DECRYPTED "+fileToDecrypt.getName().replace("ENCRYPTED ", ""));
    if(!decrypted.exists())
        decrypted.createNewFile();
    bIs = new BufferedInputStream(new FileInputStream(fileToDecrypt));
    bOs = new BufferedOutputStream(new FileOutputStream(decrypted));

    @SuppressWarnings("unused")
    int read = 0;
    byte[] buff = new byte[1388];
    while((read = bIs.read(buff)) != -1)
    {
        byte[] de = decrypt(buff);
        bOs.write(de, 0, de.length);
    }
    bIs.close();
    bOs.close();
}

private String cutPath(String path)
{
    String temp = "";
    String[] parts = path.split(Pattern.quote(File.separator));
    for(int i = 0; i < parts.length-1; i++)
        temp+=parts[i]+"/";
    return temp;
}
