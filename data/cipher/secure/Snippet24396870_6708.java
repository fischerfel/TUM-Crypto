public class crypto {
    public static void main(String [] args) {
        String s = args[0];
        String s1 = args[1];
        String ivkey = "thisisasecretkey";
        byte[] ivraw = ivkey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(ivraw, "AES");

        if (s.equalsIgnoreCase("ENCRYPT")) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(s1.getBytes());
                System.out.println(new String(Base64.encodeBase64(encrypted)));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(s1.getBytes());
                System.out.println(new String(Base64.decodeBase64(encrypted)));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return;
    };
}
