import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class RijndaelTestJava {

    private static final boolean FOR_ENCRYPTION = true;

    public static void main(String[] args) throws Exception {
        rijndael256BouncyLW();
        rijndael256BouncyProvider();
    }

    private static void rijndael256BouncyLW() throws InvalidCipherTextException {
        {
            RijndaelEngine rijndael256 = new RijndaelEngine(256);
            BufferedBlockCipher rijndael256CBC =
                    new BufferedBlockCipher(
                            new CBCBlockCipher(rijndael256));
            KeyParameter key = new KeyParameter(new byte[256 / Byte.SIZE]);
            rijndael256CBC.init(FOR_ENCRYPTION, new ParametersWithIV(key,
                    new byte[256 / Byte.SIZE]));
            byte[] in = new byte[64]; // two blocks
            byte[] out = new byte[64]; // two blocks
            int off = rijndael256CBC.processBytes(in, 0, in.length, out, 0);
            off += rijndael256CBC.doFinal(out, off);
            System.out.println(Hex.toHexString(out));
        }
    }

    private static void rijndael256BouncyProvider() throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS7Padding");
            SecretKeySpec key = new SecretKeySpec(new byte[256 / Byte.SIZE],
                    "Rijndael");
            IvParameterSpec iv = new IvParameterSpec(new byte[256 / Byte.SIZE]);
            // throws an InvalidAlgorithmParameterException: IV must be 16 bytes long.
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] out = cipher.doFinal("owlsteead"
                    .getBytes(StandardCharsets.US_ASCII));
            System.out.println(Hex.toHexString(out));
        }
    }
}
