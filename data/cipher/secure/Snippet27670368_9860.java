public class AESDecryption {

    protected SecretKeySpec getPublicKey() {

        try {
            byte[] key = "MuidKeibimbtjph9".getBytes("UTF-8");
            key = MessageDigest.getInstance("SHA-256").digest(key);
            key = Arrays.copyOf(key, 32);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] data) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(2, new SecretKeySpec(getPublicKey().getEncoded(), "AES"), new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte decryptedBytes[] = cipher.doFinal(data);
            return new String(Arrays.copyOf(decryptedBytes, decryptedBytes.length - decryptedBytes[-1 + decryptedBytes.length]));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        try {
            byte[] content = Files.readAllBytes(Paths.get("/tmp/dump.gzip"));
            AESDecryption aesDecryption = new AESDecryption();
            System.out.println(aesDecryption.decrypt(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
