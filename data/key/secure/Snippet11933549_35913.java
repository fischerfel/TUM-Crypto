try {
    // get bytes representation of the password
    key = passphrase.getBytes("UTF8");
} catch (UnsupportedEncodingException e) {
    throw new IllegalArgumentException(e);
}

key = padKeyToLength(key, MAX_KEY_LENGTH);
key = addParity(key);
keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
