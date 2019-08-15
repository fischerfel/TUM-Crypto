private static final String PASSWORD = "46dkaKLKKJLjdkdk;akdjafj";

private static final byte[] SALT = { 0x26, 0x19, (byte) 0x81, 0x4E,
        (byte) 0xA0, 0x6D, (byte) 0x95, 0x34 };

public static void main(String[] args) throws Exception {
    final Cipher desEDE = Cipher.getInstance("DESede/CBC/PKCS5Padding");

    final PasswordDeriveBytes myPass = new PasswordDeriveBytes(PASSWORD, SALT);
    final SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
    final byte[] key = myPass.getBytes(192 / Byte.SIZE);
    final SecretKey desEDEKey = kf.generateSecret(new DESedeKeySpec(key));

    final byte[] iv = myPass.getBytes(desEDE.getBlockSize());

    desEDE.init(Cipher.ENCRYPT_MODE, desEDEKey, new IvParameterSpec(iv));

    final byte[] ct = desEDE.doFinal("owlstead".getBytes(US_ASCII));
}
