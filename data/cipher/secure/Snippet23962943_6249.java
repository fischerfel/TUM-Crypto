import java.math.BigInteger;

import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSA {

private BigInteger modulus;
private BigInteger exponent;
private Cipher cipher;
private RSAPublicKeySpec pubkeyspec;
private KeyFactory factory;
private RSAPublicKey pubkey;

public RSA(BigInteger modulus, BigInteger exponent) throws Exception {
    this.modulus = modulus;
    this.exponent = exponent;
    this.init();
}

private void init() throws Exception {
    this.factory = KeyFactory.getInstance("RSA");
    this.pubkeyspec = new RSAPublicKeySpec(this.modulus, this.exponent);
    this.pubkey = (RSAPublicKey) factory.generatePublic( this.pubkeyspec );

    this.cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    this.cipher.init(Cipher.ENCRYPT_MODE, this.pubkey );

}

public byte[] encrypt(byte[] data) throws Exception {
    return this.cipher.doFinal(data);
}

public String encrypt(String data) throws Exception {
    return new String(this.encrypt(data.getBytes()));
}
}
