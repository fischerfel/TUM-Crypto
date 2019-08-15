public static void main(String[] args) {

    byte[] message;
    byte[] randomSalt;
    int iterations = 1000;

    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    // Use randomSalt Once
    digest.update(randomSalt);

    byte[] hash = digest.digest(message);

    for (int i = 0; i < iterations; i++) {

        // randomSalt Are Not used here
        hash = digest.digest(hash);
    }

    // Final result:  randomSalt + hash
}
