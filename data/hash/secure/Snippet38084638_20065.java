public static void main(String[] args) {

    byte[] message;
    byte[] randomSalt;
    int iterations = 1000;

    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    byte[] hash = digest.digest(message);

    for (int i = 0; i < iterations; i++) {

        // randomSalt used multi-times
        hash = digest.digest(ArrayUtils.appendArrays(randomSalt, hash));
    }

    // Final result:  randomSalt + hash
}
