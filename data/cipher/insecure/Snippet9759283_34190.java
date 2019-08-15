import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory


...
DESedeKeySpec k;
Cipher c;

...
k = new DESedeKeySpec("abcdefghabcdefgh".getBytes());
c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, k);
decrypted = c.doFinal("jEUQrH58Ulk=\n".getBytes());
