import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

public class EncryptedBufferedItemReaderFactory implements BufferedReaderFactory {

  private String keyFile;
  private String algorithm;

  @Override
  public BufferedReader create(final Resource resource, final String encoding) throws UnsupportedEncodingException, IOException {
    FileInputStream fis = new FileInputStream(resource.getFile());
    CipherInputStream cis = new CipherInputStream(fis, createCipher());
    return new BufferedReader(new InputStreamReader(cis, encoding));
  }

  private Cipher createCipher() throws IOException, FileNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile));
    Cipher cipher;
    try {
      DESKeySpec ks = new DESKeySpec((byte[]) ois.readObject());
      SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
      SecretKey key = skf.generateSecret(ks);
      cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec((byte[]) ois.readObject()));
    } catch (Exception ex) {
      throw new IOException(ex);
    } finally {
      ois.close();
    }
    return cipher;
  }

  public void setKeyFile(final String keyFile) {
    this.keyFile = keyFile;
  }

  public void setAlgorithm(final String algorithm) {
    this.algorithm = algorithm;
  }
}
