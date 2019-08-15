    private static byte[] raw = {-31,   17,   7,  -34,  59, -61, -60,  -16, 
                              26,   87, -35,  114,   0, -53,  99, -116, 
                             -82, -122,  68,   47,  -3, -17, -21,  -82, 
                             -50,  126, 119, -106, -119, -5, 109,   98};
    private static SecretKeySpec skeySpec;
    private static Cipher ecipher;
    private static Cipher dcipher;

    static {
        try {
            skeySpec = new SecretKeySpec(raw, "AES");
            // Instantiate the cipher
            ecipher = Cipher.getInstance("AES");
            dcipher = Cipher.getInstance("AES");
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            dcipher.init(Cipher.DECRYPT_MODE, skeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new UnhandledException("No existe el algoritmo deseado", e);
        } catch (NoSuchPaddingException e) {
            throw new UnhandledException("No existe el padding deseado", e);
        } catch (InvalidKeyException e) {
            throw new UnhandledException("Clave invalida", e);
        }
    }
