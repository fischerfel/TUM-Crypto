import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ECC_page extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecc_page);

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("ECIES", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        ECGenParameterSpec brainpoolP160R1 = new ECGenParameterSpec("brainpoolP160R1");

        try {
            assert kpg != null;
            kpg.initialize(brainpoolP160R1); //I am getting the error here
        } catch (InvalidAlgorithmParameterException ignored) {


        }

        KeyPair kp = kpg.generateKeyPair();

        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        byte[] PublicKey = publicKey.getEncoded();
        byte[] PrivateKey = privateKey.getEncoded();

        Cipher c = null;
        try {
            c = Cipher.getInstance("ECIESWithAES/DHAES/NoPadding", "BC");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            c.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] cipher = new byte[0];
        try {
            cipher = c.doFinal("This is the message".getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        TextView eccencoded = (TextView) findViewById(R.id.eccencoded);
        eccencoded.setText("[ENCODED]:\n" +
                Base64.encodeToString(cipher, Base64.DEFAULT) + "\n");


        try {
            c.init(Cipher.DECRYPT_MODE, privateKey); 
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

       byte[] plaintext = new byte[0];
        try {
            plaintext = c.doFinal(cipher);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        TextView eccdecoded = (TextView) findViewById(R.id.eccdecoded);
        eccdecoded.setText("[DECODED]:\n" +
                Base64.encodeToString(plaintext, Base64.DEFAULT) + "\n");


    }

}
