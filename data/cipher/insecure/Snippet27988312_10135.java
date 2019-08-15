private static final String CIPHER_ALGORITHM = "AES";

// nullSafeSet
protected void noNullSet(PreparedStatement st, Object value, int index, SessionImplementor si) throws SQLException {
    byte[] clearText = ((String) value).getBytes(Charset.forName("UTF-8"));

    try {
        Cipher encryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(cle));
        st.setBytes(index, encryptCipher.doFinal(clearText));
    } 
    catch (GeneralSecurityException e) {
        throw new RuntimeException("should never happen", e);
    }
}

@Override
public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor si, Object owner) throws HibernateException, SQLException {
    byte[] bytes = rs.getBytes(names[0]);
    try {
        Cipher decryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
        decryptCipher.init(Cipher.DECRYPT_MODE, getKey(cle));
        if (bytes != null) {
            return new String(decryptCipher.doFinal(bytes), Charset.forName("UTF-8"));
        } 
        else {
            return new String();
        }

    } 
    catch (GeneralSecurityException e) {
        throw new RuntimeException("Mauvaise clé");
    }
}

private static SecretKeySpec getKey(String secretKey) {
    final byte[] finalKey = new byte[16];
    int i = 0;
    for (byte b : secretKey.getBytes()) {
        // XOR
        finalKey[i++ % 16] ^= b;
    }
    return new SecretKeySpec(finalKey, "AES");
}
