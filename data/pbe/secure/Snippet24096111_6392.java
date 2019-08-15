public boolean validatePassword(String originalPassword, String storedPassword)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
    String parts[] = storedPassword.split(":");
    int iterations = Integer.parseInt(parts[0]);
    byte salt[] = fromHex(parts[1]);
    byte hash[] = fromHex(parts[2]);
    PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte testHash[] = skf.generateSecret(spec).getEncoded();
    int diff = hash.length ^ testHash.length;
    for (int i = 0; i < hash.length && i < testHash.length; i++) {
        diff |= hash[i] ^ testHash[i];
    }

    return diff == 0;
}

private static byte[] fromHex(String hex)
        throws NoSuchAlgorithmException {
    byte bytes[] = new byte[hex.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }

    return bytes;
}
