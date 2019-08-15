final byte[] iv = new byte[16]; // random would be better

OutputEncryptor encryptor = new OutputEncryptor() {
    @Override
    public OutputStream getOutputStream(OutputStream encOut) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, zmkKey, new IvParameterSpec(iv));
            return new CipherOutputStream(encOut, cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GenericKey getKey() {
        return new JceGenericKey(getAlgorithmIdentifier(), zmkKey);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmIdentifier() {
        return new AlgorithmIdentifier(
                NISTObjectIdentifiers.id_aes128_CBC,
                // AES CBC mode requires an IV, specified as an octet string
                new DEROctetString(iv));
    }
};

PKCS8Generator pkcs8Generator = new JcaPKCS8Generator(keyPair.getPrivate(), encryptor);
StringWriter sw = new StringWriter();
try (PemWriter writer = new PemWriter(sw)) {
    writer.writeObject(pkcs8Generator);
}

String pemPKCS8 = sw.toString();
