// Create secretKey using 'keysize' ...

if (encryption.isUnlimitedCrypto()) {
    Encryption.enableUnlimitedCrypto();
}

Cipher cipher = Cipher.getInstance(encryption.getCipherStr(), Encryption.PROVIDER);

if (encryption.isIvNeeded()) {
    byte[] iv = ... 
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
}
else {
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
}
