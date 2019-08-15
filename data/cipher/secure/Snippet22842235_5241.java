InputStream in = mContext.getResources().openRawResource(R.raw.key);

        CertificateFactory cf = CertificateFactory.getInstance("X509");
        Certificate cert = cf.generateCertificate(new ByteArrayInputStream(org.apache.commons.io.IOUtils.toByteArray(in)));
        PublicKey pubKey = cert.getPublicKey();
        try
        {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");            
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            final String encryptedString = Base64.encode(cipher.doFinal(message));
            return encryptedString;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";   
