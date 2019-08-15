Cipher m_cipher = Cipher.getInstance("RSA");
m_cipher.init(Cipher.ENCRYPT_MODE, publicKey);

byte[] result = m_cipher.doFinal(dataNeedToCrypt);
