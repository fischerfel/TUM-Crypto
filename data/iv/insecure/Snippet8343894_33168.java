public class crypto {

    public static void main( String[] args )
    {
        try {
            File f = new File("test.enc");
            Cipher c;
            Key k;
            String secretString = "01020304050607080900010203040506";
            String ivString = "01020304050607080900010203040506";
            byte[] secret = hexStringToByteArray(secretString);
            byte[] iv = hexStringToByteArray(ivString);

            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            k = new SecretKeySpec(secret, "AES");
            c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));

            CipherInputStream cis = new CipherInputStream(new FileInputStream(f), c);
            BufferedReader br = new BufferedReader(new InputStreamReader(cis));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println(e.getMessage());
        }

    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
                                                            33,1          71%
