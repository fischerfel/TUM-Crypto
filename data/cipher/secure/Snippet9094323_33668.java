public class MyEncrypt {

    public void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
          try {
                oout.writeObject(mod);
                oout.writeObject(exp);
          } catch (Exception e) {
          throw new IOException("Unexpected error", e);
          } finally {
            oout.close();
          }
    }

    public static void main(String[] args) throws Exception {
                MyEncrypt myEncrypt = new MyEncrypt();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(128);
        KeyPair kp = kpg.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        KeyFactory fact = KeyFactory.getInstance("RSA");        
        RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

        myEncrypt.saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
        myEncrypt.saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());
        String encString = myEncrypt.bytes2String(myEncrypt.rsaEncrypt("pritesh".getBytes()));
        System.out.println("encrypted : " + encString);
        String decString = myEncrypt.bytes2String(myEncrypt.rsaDecrypt(encString.getBytes()));
        System.out.println("decrypted : " + decString);
    }   

    PublicKey readKeyFromFile(String keyFileName) throws Exception {
      InputStream in = new FileInputStream(keyFileName);
      ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
      try {
        BigInteger m = (BigInteger) oin.readObject();
        BigInteger e = (BigInteger) oin.readObject();
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(keySpec);
        return pubKey;
      } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error", e);
      } finally {
        oin.close();
      }
    }

    PrivateKey readPrivateKeyFromFile(String keyFileName) throws Exception {
      InputStream in = new FileInputStream(keyFileName);
      ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
      try {
        BigInteger m = (BigInteger) oin.readObject();
        BigInteger e = (BigInteger) oin.readObject();
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey pubKey = fact.generatePrivate(keySpec);
        return pubKey;
      } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error", e);
      } finally {
        oin.close();
      }
    }

    public byte[] rsaEncrypt(byte[] data) throws Exception {
      byte[] src = new byte[] { (byte) 0xbe, (byte) 0xef };
      PublicKey pubKey = this.readKeyFromFile("public.key");
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      byte[] cipherData = cipher.doFinal(data);
      return cipherData;
    }

    public byte[] rsaDecrypt(byte[] data) throws Exception {
      byte[] src = new byte[] { (byte) 0xbe, (byte) 0xef };
      PrivateKey pubKey = this.readPrivateKeyFromFile("private.key");
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, pubKey);
      byte[] cipherData = cipher.doFinal(data);
      return cipherData;
    }

    private String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b: bytes) {
                String hexString = Integer.toHexString(0x00FF & b);
                string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }
}
