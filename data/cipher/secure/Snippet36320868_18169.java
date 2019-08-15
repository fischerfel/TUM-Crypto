public static PrivateKey getPrivateKey(String key) {
        try {
            /* Add PKCS#8 formatting */
            byte[] byteKey = Base64.getDecoder().decode(key.getBytes());
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1Integer(0));
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
            v2.add(DERNull.INSTANCE);
            v.add(new DERSequence(v2));
            v.add(new DEROctetString(byteKey));
            ASN1Sequence seq = new DERSequence(v);
            byte[] privKey = seq.getEncoded("DER");

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String cipherString,PrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainByte = cipher.doFinal(Base64.getDecoder().decode(cipherString));
            return Base64.getEncoder().encodeToString(plainByte);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
