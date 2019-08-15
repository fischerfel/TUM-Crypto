import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;


    String bosco = "bosco";
    byte[] initVector = Hex.decodeHex("0FDB737918DABC9FFF304DF7C288C659".toCharArray());
    byte[] first8Vector = ArrayUtils.subarray(initVector, 0, 8);
    byte[] rawPassPhrase =  ArrayUtils.addAll(first8Vector, bosco.getBytes());
    byte[] passphrase = DigestUtils.md5(rawPassPhrase);
    String body = F;

    IvParameterSpec iv = new IvParameterSpec(initVector);
    SecretKeySpec skeySpec = new SecretKeySpec(passphrase, "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

    byte[] original = cipher.doFinal(Base64.decodeBase64(body));

    System.out.println("original = " + new String(original));
