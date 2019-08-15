public class MainActivity extends Activity {
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    String desc = "starting work...";
    TextView tv1 = (TextView) findViewById(R.id.tv1);
    tv1.setText(desc);

    byte[] KEY_GENERATION_ALG = null;
    try {
        KEY_GENERATION_ALG = ("teste").getBytes("ISO-8859-1");
    } catch (UnsupportedEncodingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    final int HASH_ITERATIONS = 10000;
    final int KEY_LENGTH= 256;
    SecretKeySpec skforAES=null;
    // Obviously, you must not embed the human-friendly passphrase in your code, as I have done here.
    // The passphrase should be kept outside the mobile device, and the user prompted to enter it.
    // It is supplied as a literal here for convenience of this demonstration
    /*char [] humanPassphrase = {'P','e','r',' ','v','a','l','l','u','m',' ',
                               'd','u','c','e','s',' ','L','a','b','a','n','t' };
    byte [] salt = {0,1,2,3,4,5,6,7,8,9,0xA,0xB,0xC,0xD,0xE,0xF};  // must save this for next time we want the key


    PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt, HASH_ITERATIONS, KEY_LENGTH);
    tv1.setText("PBEKeySpec generated");
    SecretKeyFactory keyfactory=null;
    SecretKey sk=null;
    SecretKeySpec skforAES=null;
    try {
        keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
        sk = keyfactory.generateSecret(myKeyspec);

    } catch (NoSuchAlgorithmException nsae) {
        Log.e("AESdemo", "no key factory support for PBEWITHSHAANDTWOFISH-CBC" );
    } catch (InvalidKeySpecException ikse) {
        Log.e("AESdemo", "invalid key spec for PBEWITHSHAANDTWOFISH-CBC" );
    }

    // This is our secret key.  We could just save this to a file instead of regenerating it
    // each time it is needed.  But that file cannot be on the device (too insecure).  It could
    // be secure if we kept it on a server accessible through https.
    byte[] skAsByteArray = sk.getEncoded();*/
    try {
        skforAES = new SecretKeySpec(KEY_GENERATION_ALG, "AES");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

    // must save the IV for use when we want to decrypt the text
    byte [] iv = {0xA,1,0xB,5,4,0xF,7,9,0x17,3,1,6,8,0xC,0xD,91};   
    IvParameterSpec IV = new IvParameterSpec(iv);
    byte[] plaintext=null;
    try {
        // Obviously in a real use scenario, the plaintext will not be a literal in your app.
        // This plaintext comes from chapter X of "Alice's Adventures in Wonderland" by Lewis Carroll
        // I like the emphasis the author places on clear explanations ...
        plaintext = (
                  "Tis the voice of the Lobster; I heard him declare,\n"
                + "    \"You have baked me too brown, I must sugar my hair.\"\n "
                + "    As a duck with its eyelids, so he with his nose\n"
                + "    Trims his belt and his buttons, and turns out his toes.'\n"
                + "`I should like to have it explained,' said the Mock Turtle.\n"
                + "`She can't explain it,' said the Gryphon hastily. `Go on with the next verse.'\n"
                + "Alice did not dare to disobey, though she felt sure it would all come wrong, "
                + "and she went on in a trembling voice:-- \n"
                + "    `I passed by his garden, and marked, with one eye, \n"
                + "     How the Owl and the Panther were sharing a pie--' \n"
                + "`What is the use of repeating all that stuff,' the Mock Turtle interrupted, "
                + "`if you don't explain it as you go on? It's by far the most confusing thing I ever heard!'"
                ).getBytes("ISO-8859-1");
    } catch (UnsupportedEncodingException uee) {
        Log.e("AESdemo", "no String support for ISO-8859-1" );
    }

    Log.i("AESdemo", "plaintext length =" + plaintext.length );


    byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);

    String decrypted = new String( decrypt(CIPHERMODEPADDING, skforAES, IV, ciphertext) );

    tv1.setText(decrypted);
}

//  Use this method if you want to add the padding manually
//  AES deals with messages in blocks of 16 bytes.
//  This method looks at the length of the message, and adds bytes at the end
//  so that the entire message is a multiple of 16 bytes.
//  the padding is a series of bytes, each set to the total bytes added (a number in range 1..16).
byte [] addPadding (byte[] plain) {
    byte plainpad[] = null;
    int shortage  = 16 - (plain.length % 16);
    // if already an exact multiple of 16, need to add another block of 16 bytes
    if (shortage==0) shortage=16;

    // reallocate array bigger to be exact multiple, adding shortage bits.
    plainpad = new byte[ plain.length+shortage ];
    for (int i=0;i< plain.length; i++) {
        plainpad[i]=plain[i];
    }
    for (int i=plain.length;i<plain.length+shortage;i++) {
        plainpad[i]=(byte)shortage;
    }
    return plainpad;
}

//  Use this method if you want to remove the padding manually
// This method removes the padding bytes
byte [] dropPadding (byte[] plainpad) {
    byte plain[] = null;
    int drop  = plainpad[plainpad.length-1];  //last byte gives number of bytes to drop

    // reallocate array smaller, dropping the pad bytes.
    plain = new byte[ plainpad.length - drop ];
    for (int i=0;i< plain.length; i++) {
        plain[i]=plainpad[i];
        plainpad[i]=0;  // don't keep a copy of the decrypt
    }
    return plain;
}


byte [] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
    try {
        Cipher c = Cipher.getInstance(cmp);
        c.init(Cipher.ENCRYPT_MODE, sk, IV);
        return c.doFinal(msg);
    } catch (NoSuchAlgorithmException nsae) {
        Log.e("AESdemo", "no cipher getinstance support for "+cmp );
    } catch (NoSuchPaddingException nspe) {
        Log.e("AESdemo", "no cipher getinstance support for padding " + cmp );
    } catch (InvalidKeyException e) {
        Log.e("AESdemo", "invalid key exception" );
    } catch (InvalidAlgorithmParameterException e) {
        Log.e("AESdemo", "invalid algorithm parameter exception" );
    } catch (IllegalBlockSizeException e) {
        Log.e("AESdemo", "illegal block size exception" );
    } catch (BadPaddingException e) {
        Log.e("AESdemo", "bad padding exception" );
    }
    return null;
}

byte [] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
    try {
        Cipher c = Cipher.getInstance(cmp);
        c.init(Cipher.DECRYPT_MODE, sk, IV);
        return c.doFinal(ciphertext);
    } catch (NoSuchAlgorithmException nsae) {
        Log.e("AESdemo", "no cipher getinstance support for "+cmp );
    } catch (NoSuchPaddingException nspe) {
        Log.e("AESdemo", "no cipher getinstance support for padding " + cmp );
    } catch (InvalidKeyException e) {
        Log.e("AESdemo", "invalid key exception" );
    } catch (InvalidAlgorithmParameterException e) {
        Log.e("AESdemo", "invalid algorithm parameter exception" );
    } catch (IllegalBlockSizeException e) {
        Log.e("AESdemo", "illegal block size exception" );
    } catch (BadPaddingException e) {
        Log.e("AESdemo", "bad padding exception" );
    }
    return null;
}
