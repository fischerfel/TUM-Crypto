import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncryptionOutputStream extends ByteArrayOutputStream {
    private final Cipher cipher;

    public EncryptionOutputStream(SymmetricEncryptionKey symmetricEncryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        super();
        cipher = Cipher.getInstance(Config.SYMMETRIC_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricEncryptionKey.getKey(), new IvParameterSpec(symmetricEncryptionKey.getIvParameter()));
    }

    public void writeObject(Serializable object) throws IOException {
        try {
            final SealedObject sealedObject = new SealedObject(object, cipher);
            final ObjectOutputStream outputStream = new ObjectOutputStream(this);
            outputStream.writeObject(sealedObject);
        } catch (final IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

}
