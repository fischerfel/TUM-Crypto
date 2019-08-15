public BitSet encrypt(BitSet plaintextBlock, BitSet key)
{
    try {
        Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
        SecretKeyFactory sf = SecretKeyFactory.getInstance("DES");
        Key desKey = sf.generateSecret(new DESKeySpec(key.toByteArray()));
        c.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] input = plaintextBlock.toByteArray();
        byte[] encrypted = c.doFinal(input);

        return BitSet.valueOf(encrypted);
    } catch (Exception e) { /* Exception handling omitted */ }
}
