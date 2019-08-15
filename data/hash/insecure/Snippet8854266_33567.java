MessageDigest md5;
try {
    md5 = MessageDigest.getInstance("MD5");
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
    return null;
}
md5.update(new byte[] {
    (byte)(x >>> 24),
    (byte)(x >>> 16),
    (byte)(x >>> 8),
    (byte)x,
    (byte)(z >>> 24),
    (byte)(z >>> 16),
    (byte)(z >>> 8),
    (byte)z
}, 0, 8);
byte[] digest = md5.digest();
long seed = digest[0] + (digest[1] << 8) + (digest[2] << 16) + (digest[3] << 24) + (digest[4] << 32) + (digest[5] << 40) + (digest[6] << 48) + (digest[7] << 56);
Random random = new Random(seed);
