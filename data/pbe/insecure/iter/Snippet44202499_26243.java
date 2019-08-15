public byte[] DesencriptarCertificado(String pCertificado, String pClave) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException, ShortBufferException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    try {
        String CertClaveDesencriptada = DesencriptarString(pClave, "");

        ////////////Transform//////////////
        byte[] bytes = new byte[8];
        byte[] BytesClave = CertClaveDesencriptada.getBytes();
        int length = Math.min(BytesClave.length, bytes.length);

        for (int i = 0; i < length; i++) {
            bytes[i] = BytesClave[i];
        }

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec pbeKeySpec = new PBEKeySpec(CertClaveDesencriptada.toCharArray(), bytes, 12, 1000);
        Key secretKey = factory.generateSecret(pbeKeySpec);
        byte[] encoded = secretKey.getEncoded();

        byte[] KEY = new byte[32];
        byte[] IV = new byte[16];

        //ASIGNO BYTES A KEY E IV
        System.arraycopy(encoded, 0, KEY, 0, 32); 
        System.arraycopy(encoded, 32, IV, 0, 16);

        SecretKeySpec secret = new SecretKeySpec(key, "Rijndael");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(IV);
        _cipherDecrypEncrypt = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        _cipherDecrypEncrypt.init(Cipher.DECRYPT_MODE, secret, ivSpec);


        ///////////////DESENCRIPTAR CERTIFICADO/////////////////////
        byte[] beforeEncrypt = HexToByte(pCertificado);
        byte[] byteCertificadoDescencriptado = _cipherDecrypEncrypt.doFinal(beforeEncrypt);

        return byteCertificadoDescencriptado;

    } catch (InvalidKeyException e) {
        throw new TAFACE2ApiEntidad.TAException(e.getMessage());
    } catch (IllegalBlockSizeException e) {
        System.out.println(e);
        throw new TAFACE2ApiEntidad.TAException(e.getMessage());
    } catch (BadPaddingException e) {
        System.out.println(e);
        throw new TAFACE2ApiEntidad.TAException(e.getMessage());
    }

}
