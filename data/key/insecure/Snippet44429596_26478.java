public static void main(String... args) throws Exception {
    int tagSize = 96;

    Cipher gcm = Cipher.getInstance("AES/GCM/NoPadding");

    SecretKey aesKey = new SecretKeySpec(new byte[16], "AES");

    GCMParameterSpec gcmSpec = new GCMParameterSpec(tagSize, new byte[gcm.getBlockSize()]);

    gcm.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);

    byte[] pt = "Maarten Bodewes creates code".getBytes(StandardCharsets.UTF_8);
    System.out.println(pt.length);
    byte[] ctAndTag = new byte[gcm.getOutputSize(pt.length)];

    System.out.println(ctAndTag.length);

    int off = 0;
    off += gcm.update(pt, 0, pt.length, ctAndTag, off);
    // prints 16 (for the Oracle crypto provider)
    // meaning it is not online, buffering even during encryption
    System.out.println(off);
    off += gcm.doFinal(new byte[0], 0, 0, ctAndTag, off);
    // prints 40 for the Oracle crypto provider, meaning it doesn't *just*
    // output the tag during doFinal !
    System.out.println(off);

    int ctSize = ctAndTag.length - tagSize / Byte.SIZE;
    System.out.println(ctSize);

    byte[] ct = Arrays.copyOfRange(ctAndTag, 0, ctSize);
    byte[] tag = Arrays.copyOfRange(ctAndTag, ctSize, ctAndTag.length);

    System.out.println(Hex.toHexString(ct));
    System.out.println(Hex.toHexString(tag));
}
