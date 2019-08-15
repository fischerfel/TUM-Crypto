public static void main(String[] args) throws Exception {
    final byte[] data = new byte[64 * 1024];
    final byte[] encrypted = new byte[64 * 1024];
    final byte[] key = new byte[32];
    final byte[] iv = new byte[12];
    final Random random = new Random(1);
    random.nextBytes(data);
    random.nextBytes(key);
    random.nextBytes(iv);

    System.out.println("Benchmarking AES-256 GCM encryption for 10 seconds");
    long javaEncryptInputBytes = 0;
    long javaEncryptStartTime = System.currentTimeMillis();
    final Cipher javaAES256 = Cipher.getInstance("AES/GCM/NoPadding");
    byte[] tag = new byte[16];
    long encryptInitTime = 0L;
    long encryptUpdate1Time = 0L;
    long encryptDoFinalTime = 0L;
    while (System.currentTimeMillis() - javaEncryptStartTime < 10000) {
        random.nextBytes(iv);
        long n1 = System.nanoTime();
        javaAES256.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(16 * Byte.SIZE, iv));
        long n2 = System.nanoTime();
        javaAES256.update(data, 0, data.length, encrypted, 0);
        long n3 = System.nanoTime();
        javaAES256.doFinal(tag, 0);
        long n4 = System.nanoTime();
        javaEncryptInputBytes += data.length;

        encryptInitTime = n2 - n1;
        encryptUpdate1Time = n3 - n2;
        encryptDoFinalTime = n4 - n3;
    }
    long javaEncryptEndTime = System.currentTimeMillis();
    System.out.println("Time init (ns): "     + encryptInitTime);
    System.out.println("Time update (ns): "   + encryptUpdate1Time);
    System.out.println("Time do final (ns): " + encryptDoFinalTime);
    System.out.println("Java calculated at " + (javaEncryptInputBytes / 1024 / 1024 / ((javaEncryptEndTime - javaEncryptStartTime) / 1000)) + " MB/s");

    System.out.println("Benchmarking AES-256 GCM decryption for 10 seconds");
    long javaDecryptInputBytes = 0;
    long javaDecryptStartTime = System.currentTimeMillis();
    final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * Byte.SIZE, iv);
    final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    long decryptInitTime = 0L;
    long decryptUpdate1Time = 0L;
    long decryptUpdate2Time = 0L;
    long decryptDoFinalTime = 0L;
    while (System.currentTimeMillis() - javaDecryptStartTime < 10000) {
        long n1 = System.nanoTime();
        javaAES256.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        long n2 = System.nanoTime();
        int offset = javaAES256.update(encrypted, 0, encrypted.length, data, 0);
        long n3 = System.nanoTime();
        javaAES256.update(tag, 0, tag.length, data, offset);
        long n4 = System.nanoTime();
        javaAES256.doFinal(data, offset);
        long n5 = System.nanoTime();
        javaDecryptInputBytes += data.length;

        decryptInitTime += n2 - n1;
        decryptUpdate1Time += n3 - n2;
        decryptUpdate2Time += n4 - n3;
        decryptDoFinalTime += n5 - n4;
    }
    long javaDecryptEndTime = System.currentTimeMillis();
    System.out.println("Time init (ns): " + decryptInitTime);
    System.out.println("Time update 1 (ns): " + decryptUpdate1Time);
    System.out.println("Time update 2 (ns): " + decryptUpdate2Time);
    System.out.println("Time do final (ns): " + decryptDoFinalTime);
    System.out.println("Total bytes processed: " + javaDecryptInputBytes);
    System.out.println("Java calculated at " + (javaDecryptInputBytes / 1024 / 1024 / ((javaDecryptEndTime - javaDecryptStartTime) / 1000)) + " MB/s");
}
