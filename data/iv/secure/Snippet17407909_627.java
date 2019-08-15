public static byte[] tripleDes(final byte[] original, final int mode, final SecretKeySpec keySpec, final byte[] ivSpec) throws GeneralSecurityException {
    final Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
    final IvParameterSpec iv = new IvParameterSpec(ivSpec);
    cipher.init(mode, keySpec, iv);
    return cipher.doFinal(original);
}
