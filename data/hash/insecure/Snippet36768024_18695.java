try (FileInputStream inputStream = new FileInputStream(file)) {
    MessageDigest digest = MessageDigest.getInstance("MD5");

    byte[] bytesBuffer = new byte[1024];
    int bytesRead = -1;

    while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
        digest.update(bytesBuffer, 0, bytesRead);
    }

    byte[] hashedBytes = digest.digest();

    return convertByteArrayToHexString(hashedBytes);
} catch (NoSuchAlgorithmException | IOException ex) {
    throw new HashGenerationException(
            "Could not generate hash from file", ex);
}
