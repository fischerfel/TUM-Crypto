import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;
import org.bouncycastle.util.encoders.Hex;
import org.hive2hive.core.H2HJUnitTest;
import org.junit.Test;

public class EncryptionUtil2Test {

    @Test
    public void testBug() throws IOException, InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, DataLengthException, IllegalStateException, InvalidCipherTextException,
        NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
        InvalidAlgorithmParameterException {

        Security.addProvider(new BouncyCastleProvider());

        // generate RSA keys
        BigInteger publicExp = new BigInteger("10001", 16); // Fermat F4, largest known fermat prime
        JDKKeyPairGenerator gen = new JDKKeyPairGenerator.RSA();
        RSAKeyGenParameterSpec params = new RSAKeyGenParameterSpec(2048, publicExp);
        gen.initialize(params, new SecureRandom());
        KeyPair keyPair = gen.generateKeyPair();

        // some data where first entry is 0
        byte[] data = { 0, 122, 12, 127, 35, 58, 87, 56, -6, 73, 10, -13, -78, 4, -122, -61 };

        // encrypt data asymmetrically
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] rsaEncryptedData = cipher.doFinal(data);

        // decrypt data asymmetrically
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] dataBack = cipher.doFinal(rsaEncryptedData);

        System.out.println("data      = " + Hex.toHexString(data));
        System.out.println("data back = " + Hex.toHexString(dataBack));

        assertTrue(Arrays.equals(data, dataBack));

    }

}
