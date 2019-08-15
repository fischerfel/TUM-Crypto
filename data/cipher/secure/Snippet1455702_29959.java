import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DHExample
{

  public static void main(String... argv)
    throws Exception
  {
    if (argv.length == 0)
      argv = new String[]{"swright,password,transfer(10000USD,erickson)"};

    /* In reality, the private key should be stored in a KeyStore, protected by 
     * a strong password that is provided interactively every time the server 
     * is started.
     */
    PrivateKey pvt = Server.loadDemoKey();
    Server server = new Server(pvt);

    for (String message : argv) {
      Client client = new Client();
      byte[] packet = client.encrypt(message);
      /* The client (phone) would then send the packet to the server... */
      /* ... Now, at the server: */
      System.out.println(server.decrypt(packet));
    }

  }

}

class Client
{

  /* This public key corresponds to the private key used by the server. 
   * Generate your own Diffie-Hellman key pair, encode the public key, and 
   * embed it here.
   */
  private static final String server = "1GG80QCC4204DGC29AGP48DTOD041G2C42046050C103UNUKS13LQH4AAIRT59OBNCSJJVC4DNA8UEUH00OCF3V05MA4J6IHAT80H53UQP7M6LHULVONQRKC7MPEDLAR6NG4TO079KDVP6CO5NDECL19D4JUFUG13R20HC4JTRL7BVTDU63FS3MLV7OQKAC58F0JTO7TMJOKFC60HLAG9LK5KH6BR7BSTE5DGTEANFU8H066CTQ5403HO2G60G1TV1K22TD6PTRR5RPAQS6QS5FEBPIINRNUHQTA1FILQC1CUGF0J7A5CLF3LQQHCKVPJH0S88305K94B728V89GK1C4TNPS4J5368KRGJO5JQHDA7P398S2HQS7HBMEJ7B4BEKDVGNUH16LHF3UR2F80I8EUCKJORTA2HI24QH0UVS5DEB7O6IA5MCNK0FDAIAP019GTVTJQ9581040G00E0O8002G600TNIVRQ4KUFKVHTM685L9LRNURUUJ8B1EC45DN5TM70MI0T8RM17QCBSQ2F1FMQM3K63MVVQBMVD7N5MRVMGRVM09B1LGNU92SMS42RSROH3FMP4532CMDHO6E72ICI7USNGGDHPFLTSS7AAFRM9T8E6ELU4E3P5EBA6JPDRUN0PDNG08HBVSRJM5U7VVAQ1PD8FIJMSN9EM3";

  public byte[] encrypt(String message)
    throws Exception
  {
    /* Convert the embedded server public key into a DHPublickKey object. */
    KeyFactory kf = KeyFactory.getInstance("DiffieHellman");
    byte[] decoded = new BigInteger(Client.server, 32).toByteArray();
    DHPublicKey server = (DHPublicKey) kf.generatePublic(new X509EncodedKeySpec(decoded));
    /* Generate an ephemeral key pair with the same parameters as server's. */
    KeyPairGenerator gen = KeyPairGenerator.getInstance("DiffieHellman");
    gen.initialize(server.getParams());
    KeyPair ephemeral = gen.generateKeyPair();
    /* Encode the client public key to be sent to server with ciphertext. */
    byte[] pub = ephemeral.getPublic().getEncoded();
    /* Generate a secret key using Diffie-Hellman between client and server. */
    KeyAgreement ka = KeyAgreement.getInstance("DiffieHellman");
    ka.init(ephemeral.getPrivate());
    ka.doPhase(server, true);
    byte[] raw = ka.generateSecret();
    SecretKeySpec secret;
    try {
      secret = new SecretKeySpec(raw, 0, 16, "AES");
    }
    finally {
      Arrays.fill(raw, (byte) 0);
    }
    /* Setup cipher for encryption with secret key. */
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    /* Get the IV to be sent to server with ciphertext. */
    byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
    /* Encrypt the message with the secret key. */
    byte[] plaintext = message.getBytes("UTF-8");
    byte[] ciphertext;
    try {
      ciphertext = cipher.doFinal(plaintext);
    }
    finally {
      Arrays.fill(plaintext, (byte) 0);
    }
    /* Package up ephemeral public key, iv, and cipher text to transmit to server. */
    byte[] packet = new byte[4 + pub.length + iv.length + ciphertext.length];
    for (int idx = 0; idx < 4; ++idx)
      packet[idx] = (byte) (pub.length >>> 8 * idx);
    System.arraycopy(pub, 0, packet, 4, pub.length);
    System.arraycopy(iv, 0, packet, 4 + pub.length, iv.length);
    System.arraycopy(ciphertext, 0, packet, 4 + pub.length + iv.length, ciphertext.length);
    return packet;
  }


}

class Server
{

  /* This key isn't private anymore, since it was posted on stackoverflow.com.
   * Generate your own Diffie-Hellman key pair, encode the private key, and 
   * embed it here. In real code, you don't embed private keys like this. This 
   * is just to enable the demonstration.
   */
  private static final String demo = "310G1CS10201GG80HM1G95A34H1NN1K0G609GG80GO0K1G40FQVQJG4ENA4H9ABFKL71ETJIEFTGHMT93PRQ4031HSFS0MP8ICQA5BL024KFRB4UOQM7QNV2VBEHGUR5PMLBCQU0JN00T6HNV4PJ0MTLPIK55KIFPVQ04FC825GIFNEKTFVLNOODVGEQNSV3AH9GL1S2FN0VMQF2HTGO26LA16MGMI4PFCTFJLOLM3LPATVP240OPJN8KG0E70A0O207NS6G8BLKR7NFCNF5BBGRBGLTPF6AAVEVQ7BL85UAN9G5JQ1S2CT8LILSENBA5IJV6E43H10C0MH4HCS93T162G5GJMV7GICKCP2JE2F0MFA5L8V4D53GA7BGU5EPQCTCHDQHNU2VQ44QM5SFRC9T0291RPIIF3FL8A688JA43RVGLLPCV0Q98MPIUG1TLA9B40563NVMF94L040G2002460I103FO2B2UAKGMNI68O6B43G31SRIBA2NGIV0BEUVD4A85NIC25AVUGP3AG6LJVFHPOJ4JQ3DJA0PFHA2V6E2U80HNHFDG157KP0NJLE7U";

  private final PrivateKey pvt;

  static PrivateKey loadDemoKey()
    throws Exception
  {
    /* Convert the embedded server public key into a PrivateKey object. */
    KeyFactory kf = KeyFactory.getInstance("DiffieHellman");
    byte[] decoded = new BigInteger(Server.demo, 32).toByteArray();
    return kf.generatePrivate(new PKCS8EncodedKeySpec(decoded));
  }

  Server(PrivateKey pvt)
  {
    this.pvt = pvt;
  }

  public String decrypt(byte[] packet)
    throws Exception
  {
    /* Deconstruct packet. */
    int len = 0;
    for (int idx = 0; idx < 4; ++idx)
      len |= ((packet[idx] & 0xFF) << 8 * idx);
    byte[] pub = new byte[len];
    System.arraycopy(packet, 4, pub, 0, len);
    KeyFactory kf = KeyFactory.getInstance("DiffieHellman");
    PublicKey client = kf.generatePublic(new X509EncodedKeySpec(pub));
    IvParameterSpec iv = new IvParameterSpec(packet, len + 4, 16);
    /* Perform key agreement to determine secret key. */
    KeyAgreement ka = KeyAgreement.getInstance("DiffieHellman");
    ka.init(pvt);
    ka.doPhase(client, true);
    byte[] raw = ka.generateSecret();
    SecretKeySpec secret;
    try {
      secret = new SecretKeySpec(raw, 0, 16, "AES");
    }
    finally {
      Arrays.fill(raw, (byte) 0);
    }
    /* Setup cipher for decryption with secret key. */
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, iv);
    byte[] plaintext = cipher.doFinal(packet, len + 20, packet.length - (len + 20));
    try {
      return new String(plaintext, "UTF-8");
    }
    finally {
      Arrays.fill(plaintext, (byte) 0);
    }
  }

}
