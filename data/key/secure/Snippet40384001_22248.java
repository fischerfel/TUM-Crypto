import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;


public class SecondActivity extends AppCompatActivity {

    static final String TAG = "SimpleKeystoreApp";
    static final String CIPHER_PROVIDER = "AndroidOpenSSL";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    private static final String KEY_ALIAS = "this is my alias";
    private static final String SHARED_PREFENCE_NAME = "my shared_prefs";
    private static final String ENCRYPTEDKEY_KEY = "encrypted_key";
    private static final String TO_BE_ENCRYPTED_KEY = "this is my test";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        createNewKeys();

        try {
            generateAndStoreAESKey();
        } catch (Exception e) {
            Log.d(TAG, "couldn't generateAndStoreAESKey:" + e.getMessage());
        }
        String encrypted = null;
        try {
            encrypted = encrypt(getApplicationContext(), TO_BE_ENCRYPTED_KEY.getBytes());
            Log.d(TAG, "encrypted:" + encrypted);
        } catch (Exception e) {
            Log.d(TAG, "couldn't encrypt:" + e.getMessage());
        }

        try {
            decrypt(getApplicationContext(), encrypted.getBytes());
            Log.d(TAG, "decrypted:" + encrypted);
        } catch (Exception e) {
            Log.d(TAG, "couldn't decrypt:" + e.getMessage());
        }
        setContentView(R.layout.activity_main);
    }

    public void createNewKeys() {
        Log.d(TAG, "___ createNewKeys");
        try {
            // Create new key if needed
            // Generate the RSA key pairs
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
            // Generate the RSA key pairs
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                // Generate a key pair for encryption
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
                        .setAlias(KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE);
                kpg.initialize(spec);
                kpg.generateKeyPair();

                KeyPair keyPair = kpg.generateKeyPair();
                Log.d(TAG, "Public Key is: " + keyPair.getPublic().toString());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void generateAndStoreAESKey() throws Exception{
        Log.d(TAG, "___ generateAndStoreAESKey");

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTEDKEY_KEY, null);
        if (enryptedKeyB64 == null) {
            byte[] key = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(key);
            byte[] encryptedKey = rsaEncrypt(key);
            enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(ENCRYPTEDKEY_KEY, enryptedKeyB64);
            edit.commit();
        }
    }
    private SecretKeySpec getSecretKey(Context context) throws Exception{
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTEDKEY_KEY, null);
        // need to check null, omitted here
        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey);
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(Context context, byte[] input) throws Exception{
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context));
        byte[] encodedBytes = c.doFinal(input);
        String encryptedBase64Encoded =  Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        return encryptedBase64Encoded;
    }


    public byte[] decrypt(Context context, byte[] encrypted) throws Exception{
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.DECRYPT_MODE, getSecretKey(context));
        byte[] decodedBytes = c.doFinal(encrypted);
        return decodedBytes;
    }


    private byte[] rsaEncrypt(byte[] secret) throws Exception{
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE, CIPHER_PROVIDER);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        return vals;
    }

    private  byte[]  rsaDecrypt(byte[] encrypted) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        Cipher output = Cipher.getInstance(RSA_MODE, CIPHER_PROVIDER);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }
}
