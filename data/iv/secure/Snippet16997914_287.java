public class MainClass {
  public static void main(String args[]) throws Exception {
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("keyfile"));
    DESKeySpec ks = new DESKeySpec((byte[]) ois.readObject());
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey key = skf.generateSecret(ks);

    Cipher c = Cipher.getInstance("DES/CFB8/NoPadding");
    c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec((byte[]) ois.readObject()));
    CipherInputStream cis = new CipherInputStream(new FileInputStream("ciphertext"), c);
    BufferedReader br = new BufferedReader(new InputStreamReader(cis));
    System.out.println(br.readLine());
  }
}
