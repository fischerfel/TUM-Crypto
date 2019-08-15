SecretKeySpec getKey() {
    final pass = "47e7717f0f37ee72cb226278279aebef".getBytes("UTF-8");
    final sha = MessageDigest.getInstance("SHA-256");

    def key = sha.digest(pass);
    // use only first 128 bit (16 bytes). By default Java only supports AES 128 bit key sizes for encryption.
    // Updated jvm policies are required for 256 bit.
    key = Arrays.copyOf(key, 16);
    return new SecretKeySpec(key, AES);
}
