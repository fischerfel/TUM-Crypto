public static void main(String[] args) {
        String s = "with the lights out is less dangerous here we are now entertain us";
        s = cripta(s);
        System.out.println(s);
        s = decripta(s);
        System.out.println(s);
    }

    public static String cripta(String s) {
        System.out.println("lunghezza stringa:" + s.length());
        byte[] input = s.getBytes();
        byte[] keyBytes = hexStringToByteArray("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        String out = "";

        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        try {
            Cipher cp = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cp.init(Cipher.ENCRYPT_MODE, key);
            byte[] criptati = cp.doFinal(input);
            out = new String(Base64.getEncoder().encode(criptati));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static String decripta(String s) {

        byte[] input = Base64.getDecoder().decode(s.getBytes());
        byte[] keyBytes = hexStringToByteArray("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        String out = "";

        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cp = Cipher.getInstance("DESede/ECB/NoPadding");
            cp.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypt = cp.doFinal(input);
            out = new String(decrypt);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return out;
    }
