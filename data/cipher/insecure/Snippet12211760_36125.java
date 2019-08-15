char[] password = new char[] { 'r', 'u', 'b', 'i', 'c', 'o', 'n' };
byte[] raw = encrypt(password,"06.93308" );

    private static byte[] encrypt(char[] password, String plaintext) throws Exception {
            byte[] bytes = new byte[password.length];
            for (int i = 0; i < password.length; ++i) {
                    bytes[i] = (byte) password[i];
            }
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes());
            return encrypted;
    }
