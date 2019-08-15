public void saveKey(Key key, String alias) {
    KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(new SecretKeySpec(key.getMaterial(), "AES"));

    try {
        keyStore.setEntry(alias, entry, new KeyStore.PasswordProtection(password));
    } catch (KeyStoreException e) {
        throw new UnexpectedErrorException("Failed to save the key", e);
    }
}
