import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import de.flexiprovider.common.ies.IESParameterSpec;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.ec.parameters.CurveParams;
import de.flexiprovider.ec.parameters.CurveRegistry.BrainpoolP384r1;
import de.flexiprovider.pki.PKCS8EncodedKeySpec;
import de.flexiprovider.pki.X509EncodedKeySpec;

public class MainActivity extends Activity {

private static PublicKey PublicKey;
private static PrivateKey PrivateKey;
private static String PubKey;
private static String PrvKey;
private static String message = "Hello World";
private static String encryptedMessage;
private static String decryptedMessage;

private final static String TAG = "ERROR: ";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    try {
        Security.addProvider(new FlexiCoreProvider());
        Security.addProvider(new FlexiECProvider());

        // instantiate the elliptic curve key pair generator
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");

        // choose the curve
        CurveParams ecParams = new BrainpoolP384r1();

        // Initialize the key pair generator
        kpg.initialize(ecParams, new SecureRandom());
        KeyPair keyPair = kpg.generateKeyPair();

        // generate the public key
        PublicKey = keyPair.getPublic();

        // generate private key
        PrivateKey = keyPair.getPrivate();
    }
    catch (Exception e) {
        Log.e(TAG, e.toString());
    }

    // I'm converting keys to strings here as the public keys will be stored on a server
    // database and the private keys will be stored in the application preferences file
    // this private key storage is maybe not optimum, but at this point I just want to
    // simulate a messaging encryption/decryption process for testing purposes

    // convert public key to a string
    PubKey = Base64.encodeToString(PublicKey.getEncoded(), Base64.DEFAULT);
    Log.d("PubKey: ", PubKey);

    // convert private key to a string
    PrvKey = Base64.encodeToString(PrivateKey.getEncoded(), Base64.DEFAULT);
    Log.d("PrvKey: ", PrvKey);

    // encrypt the message with the public key
    encryptedMessage = encryptMessage(PubKey, message);

    // report if the public key has not been regenerated correctly
    if (encryptedMessage == null) {
        Log.d("PUBLIC_KEY_REGENERATE_ERROR: ", encryptedMessage);
    }

    // decrypt the message with the private key
    decryptedMessage = decryptMessage(PrvKey, encryptedMessage);

    // report if the private key has not been regenerated correctly
    if (encryptedMessage == null) {
        Log.d("PRIVATE_KEY_REGENERATE_ERROR: ", decryptedMessage);
    }
}

// encrypt function
public static String encryptMessage(String publicKey, String message) {

    KeyFactory keyFactory = null;
    PublicKey pubkey = null;
    Cipher cipher = null;

    byte[] PLAINTEXT_MESSAGE = message.getBytes();
    Log.d("PLAINTEXT_MESSAGE: ", message);

    Security.addProvider(new FlexiCoreProvider());
    Security.addProvider(new FlexiECProvider());

    // Base64 decode the publicKey string into a byte array
    byte[] decodedPublicKey = Base64.decode(publicKey, Base64.DEFAULT);

    try {
        // instantiate a X509EncodedKeySpec
        X509EncodedKeySpec X509spec = new X509EncodedKeySpec(decodedPublicKey);

        keyFactory = KeyFactory.getInstance("ECIES", "FlexiEC");

        // re-generate the public key
        pubkey = keyFactory.generatePublic(X509spec);

        // sanity check, return null on inequality
        if (!pubkey.equals(PublicKey)) {
            return null;
        }

        cipher = Cipher.getInstance("ECIES", "FlexiEC");
        IESParameterSpec IESspec = new IESParameterSpec("AES256_CBC", "HmacSHA512", null, null);
        cipher.init(Cipher.ENCRYPT_MODE, pubkey, IESspec);
    }
    catch (Exception e) {
        Log.e(TAG, e.toString());
    }

    // encrypt the message
    byte[] encryptedData = null;

    try {
        encryptedData = cipher.doFinal(PLAINTEXT_MESSAGE);
    }
    catch (IllegalBlockSizeException e) {
        Log.e(TAG, e.toString());
    }
    catch (BadPaddingException e) {
        Log.e(TAG, e.toString());
    }

    String encryptedMessage = null;

    try {
        encryptedMessage = new String(encryptedData, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
        Log.e(TAG, e.toString());
    }
    Log.d("encryptedMessage: ", encryptedMessage);
    return encryptedMessage;
}

// decrypt function
public static String decryptMessage(String privateKey, String message) {

    KeyFactory keyFactory = null;
    PrivateKey prvkey = null;
    Cipher cipher = null;

    byte[] ENCRYPTED_MESSAGE = message.getBytes();
    Log.d("ENCRYPTED_MESSAGE: ", message);

    Security.addProvider(new FlexiCoreProvider());
    Security.addProvider(new FlexiECProvider());

    try {
        // Base64 decode the privateKey string into a byte array
        byte[] decodedPrivateKey = Base64.decode(privateKey, Base64.DEFAULT);

        // instantiate a PKCS8EncodedKeySpec
        PKCS8EncodedKeySpec PKCS8spec = new PKCS8EncodedKeySpec(decodedPrivateKey);

        keyFactory = KeyFactory.getInstance("ECIES", "FlexiEC");

        // re-generate the private key
        prvkey = keyFactory.generatePrivate(PKCS8spec);

        // sanity check, return null on inequality
        if (!prvkey.equals(PrivateKey)) {
            return null;
        }

        cipher = Cipher.getInstance("ECIES", "FlexiEC");
        IESParameterSpec IESspec = new IESParameterSpec("AES256_CBC", "HmacSHA512", null, null);
        cipher.init(Cipher.DECRYPT_MODE, prvkey, IESspec);
    }
    catch (Exception e) {
        Log.e(TAG, e.toString());
    }

    // decrypt the message
    byte[] decryptedData = null;

    try {
        decryptedData = cipher.doFinal(ENCRYPTED_MESSAGE);

        // ERROR THROWN HERE! ..............................
        // de.flexiprovider.api.exceptions.BadPaddingException: invalid ciphertext
    }
    catch (IllegalBlockSizeException e) {
        Log.e(TAG, e.toString());
    }
    catch (BadPaddingException e) {
        Log.e(TAG, e.toString());
    }

    String decryptedMessage = null;

    try {
        decryptedMessage = new String(decryptedData, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
        Log.e(TAG, e.toString());
    }
    Log.d("decryptedMessage: ", decryptedMessage);
    return decryptedMessage;
}
