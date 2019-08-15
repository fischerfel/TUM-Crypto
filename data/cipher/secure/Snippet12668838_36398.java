    public void onCreate(Bundle icicle)
{
    super.onCreate(icicle);
    setContentView(R.layout.agapplogin);

    TextView lblMobileNo = (TextView)findViewById(R.id.lblMobileNo);
    lblMobileNo.setTextColor(getResources().getColor(R.color.text_color_red));

    mobile = (EditText)findViewById(R.id.txtMobileNo);

    TextView lblPinNo = (TextView)findViewById(R.id.lblPinNo);
    lblPinNo.setTextColor(getResources().getColor(R.color.text_color_red));

    pin = (EditText)findViewById(R.id.txtPinNo);

    btnLogin = (Button)findViewById(R.id.btnLogin);
    btnClear = (Button)findViewById(R.id.btnClear);

    btnLogin.setOnClickListener(new OnClickListener() {
         public void onClick(View view) {                
             postLoginData();
         }
         });      

                ;}  

public void postLoginData() 

   {
             Intent i = new   
   Intent(this.getApplicationContext(),new.class);
             Bundle bundle = new Bundle();
             bundle.putString("mno", mobile.getText().toString());
             bundle.putString("pinno", pin.getText().toString());
             i.putExtras(bundle);
             startActivity(i);          }
}


            ///this is RSA code which i found





   import java.io.BufferedOutputStream;
   import java.io.FileOutputStream;
   import java.io.IOException;
   import java.io.ObjectOutputStream;
   import java.math.BigInteger;
   import java.security.Key;
   import java.security.KeyFactory;
   import java.security.KeyPair;
   import java.security.KeyPairGenerator;
   import java.security.NoSuchAlgorithmException;
   import java.security.spec.RSAPrivateKeySpec;
   import java.security.spec.RSAPublicKeySpec;

        /**
        * This class generates key pair and saves in file in current directory
       * 
       * @author amit
       * 
       */
        public class RSAKeyGenerator {

public void generateKeyPair(String publicKeyFileName,
        String privateKeyFileName) {
    KeyPairGenerator kpg = null;
    try {
        kpg = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    kpg.initialize(2048);
    KeyPair kp = kpg.genKeyPair();
    Key publicKey = kp.getPublic();
    Key privateKey = kp.getPrivate();
    try {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
                RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
                RSAPrivateKeySpec.class);

        saveToFile(publicKeyFileName, pub.getModulus(),
                pub.getPublicExponent());
        System.out.println("public key saved");
        saveToFile(privateKeyFileName, priv.getModulus(),
                priv.getPrivateExponent());
        System.out.println("private key saved");
    } catch (Exception e) {
        System.out.println("error ");
    }
}

private void saveToFile(String fileName, BigInteger mod, BigInteger exp)
        throws IOException {
    ObjectOutputStream oout = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(fileName)));
    try {
        oout.writeObject(mod);
        oout.writeObject(exp);
    } catch (Exception e) {
        throw new IOException("Unexpected error", e);
    } finally {
        oout.close();
    }
}

/**
 * @param args
 */
public static void main(String[] args) {
    // TODO Auto-generated method stub
    RSAKeyGenerator t = new RSAKeyGenerator();
    t.generateKeyPair("public.key", "private.key");

}

        }








        import javax.crypto.Cipher;

         public class RSAEncDec {

private PublicKey readPublicKeyFromFile(String fileloc) throws IOException {
    InputStream in = new FileInputStream(new File(fileloc));

    ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
            in));
    try {
        BigInteger m = (BigInteger) oin.readObject();
        BigInteger e = (BigInteger) oin.readObject();
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(keySpec);
        return pubKey;
    } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error", e);
    } finally {
        oin.close();
    }
}

private PrivateKey readPrivateKeyFromFile(String fileLoc)
        throws IOException {
    InputStream in = new FileInputStream(new File(fileLoc));

    ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
            in));
    try {
        BigInteger m = (BigInteger) oin.readObject();
        BigInteger e = (BigInteger) oin.readObject();
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey prvKey = fact.generatePrivate(keySpec);
        return prvKey;
    } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error", e);
    } finally {
        oin.close();
    }
}

public byte[] rsaEncrypt(byte[] data, String fileLoc) {
    try {
        PublicKey pubKey = readPublicKeyFromFile(fileLoc);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    } catch (Exception e) {
        System.out.println("some thing went wrong");
    }
    return null;
}

public byte[] rsaDecrypt(byte[] data, String fileLoc) {
    try {
        PrivateKey pubKey = readPrivateKeyFromFile(fileLoc);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    } catch (Exception e) {
        System.out.println("some thing went wrong");
    }
    return null;
}

/*
 * string
 * 
 * @param data (normal string)
 * 
 * @return
 */
public String encrypt(String data, String fileLoc) {
    byte[] enc = rsaEncrypt(data.getBytes(), fileLoc);
    return Base64.encode(enc);
}

/**
 * return the normal string
 * 
 * @param data
 *            (base64 encoded string)
 * @return
 * @throws Base64DecodingException
 */
public String decrypt(String data, String fileLoc)
        throws Base64DecodingException {
    byte[] enc = Base64.decode(data);
    byte[] decryptedData = rsaDecrypt(enc, fileLoc);
    return new String(decryptedData);
}

/**
 * 
 * @param args
 */
public static void main(String[] args) {
    // TODO Auto-generated method stub
    RSAEncDec obj = new RSAEncDec();
    String enc = obj.encrypt("hello world", "./public.key");
    System.out.println(enc);
    String dat = null;
    try {
        dat = obj.decrypt(enc, "./private.key");
    } catch (Base64DecodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    System.out.println(dat);

        }

       }
