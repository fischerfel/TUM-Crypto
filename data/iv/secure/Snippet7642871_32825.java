Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
SecretKeySpec keySpec = new SecretKeySpec(aeskey, "AES");
IvParameterSpec ivSpec = new IvParameterSpec(iv);

c.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

CipherOutputStream cipher_out = new CipherOutputStream(output, c);

try {
    while (true) {
        cipher_out.write(input.readByte());
    }
} catch (EOFException e) {
}

byte curIV[] = c.getIV();
