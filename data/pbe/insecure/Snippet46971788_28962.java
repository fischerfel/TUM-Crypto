@Test
public void shouldBeThreadSafe() {

    final byte[] encoded = {
        27, 26, 18, 88, 84, -87, -40, -91, 70, -74, 87, -21, -124,
        -114, -44, -24, 7, -7, 104, -26, 45, 96, 119, 45, -74, 51
    };
    final String expected = "dummy data";
    final Charset charset = StandardCharsets.UTF_8;

    final String salt = "e47312da-bc71-4bde-8183-5e25db6f0987";
    final String passphrase = "dummy-passphrase";

    // Crypto configuration
    final int iterationCount = 10;
    final int keyStrength = 128;
    final String pbkdf2Algorithm = "PBKDF2WithHmacSHA1";
    final String cipherAlgorithm = "AES/CFB/NoPadding";
    final String keyAlgorithm = "AES";

    // Counters
    final AtomicInteger succeedCount = new AtomicInteger(0);
    final AtomicInteger failedCount = new AtomicInteger(0);

    // Test
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
    IntStream.range(0, 1000000).parallel().forEach(i -> {
        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkdf2Algorithm);
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(charset), iterationCount, keyStrength);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec key = new SecretKeySpec(tmp.getEncoded(), keyAlgorithm);
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);


            int blockSize = cipher.getBlockSize();
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(encoded, blockSize));
            byte[] dataToDecrypt = Arrays.copyOfRange(encoded, blockSize, encoded.length);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] utf8 = cipher.doFinal(dataToDecrypt);

            String decoded = new String(utf8, charset);
            if (!expected.equals(decoded)) {
                System.out.println("Try #" + i + " | Unexpected decoded value: [" + decoded + "]");
                failedCount.incrementAndGet();
            } else {
                succeedCount.incrementAndGet();
            }
        } catch (Exception e) {
            System.out.println("Try #" + i + " | Decode failed");
            e.printStackTrace();
            failedCount.incrementAndGet();
        }
    });

    System.out.println(failedCount.get() + " of " + (succeedCount.get() + failedCount.get()) + " decodes failed");
}
