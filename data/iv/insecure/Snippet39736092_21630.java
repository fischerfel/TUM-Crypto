    public class AESTest {
           private static String AESStr = "<Encrypted KEY>";
           public static void main(String[] args) throws Exception {
                    Security.addProvider(new    org.bouncycastle.jce.provider.BouncyCastleProvider());
                System.out.println(decrypt(AESStr, "Test"));
           }
        public static String decrypt(String responseStr, String passPhrase) throws        GeneralSecurityException {
             String decryptedStr = "";
             try {
                        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, passPhrase);
                        byte[] decoded = Base64.decodeBase64(responseStr.getBytes());
                        byte[] decryptedWithKey = cipher.doFinal(decoded);
                        byte[] decrypted = Arrays.copyOfRange(decryptedWithKey, 16, decryptedWithKey.length);
                        decryptedStr = new String(decrypted, "UTF-8");
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return decryptedStr;
        }

        private static Cipher getCipher(int mode, String passPhrase) throws Exception {
                SecretKeySpec secretKeySpec = new SecretKeySpec(passPhrase.getBytes(), "AES");
                byte[] IV = new byte[16];
                new Random().nextBytes(IV);
                AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(mode, secretKeySpec, paramSpec);
                return cipher;
        }
}
