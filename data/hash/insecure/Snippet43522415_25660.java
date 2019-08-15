        // Make the digest
        InputStream data;
        try {

            data = signatureAppearance.getRangeStream();
        } catch (IOException e) {
            String message = "MessageDigest error for signature input, type: IOException";
            signLogger.logError(message, e);
            throw new CustomException(message, e);
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");

        } catch (NoSuchAlgorithmException ex) {
            String message = "MessageDigest error for signature input, type: NoSuchAlgorithmException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        }
        byte[] buf = new byte[8192];
        int n;
        try {
            while ((n = data.read(buf)) > 0) {
                messageDigest.update(buf, 0, n);
            }
        } catch (IOException ex) {
            String message = "MessageDigest update error for signature input, type: IOException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        }
        byte[] hash = messageDigest.digest();
        // If we add a time stamp:
        // Create the signature
        PdfPKCS7 sgn;
        try {

            sgn = new PdfPKCS7(key, chain, configuration.getSignCertificate().getSignatureHashAlgorithm().value() , null, new BouncyCastleDigest(), false);
        } catch (InvalidKeyException ex) {
            String message = "Certificate PDF sign error for signature input, type: InvalidKeyException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        } catch (NoSuchProviderException ex) {
            String message = "Certificate PDF sign error for signature input, type: NoSuchProviderException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        } catch (NoSuchAlgorithmException ex) {
            String message = "Certificate PDF sign error for signature input, type: NoSuchAlgorithmException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        }catch (Exception ex) {
            String message = "Certificate PDF sign error for signature input, type: Exception";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        }
        byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, null,null, MakeSignature.CryptoStandard.CMS);
        try {
            sgn.update(sh, 0, sh.length);
        } catch (java.security.SignatureException ex) {
            String message = "Certificate PDF sign error for signature input, type: SignatureException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        }
        byte[] encodedSig = sgn.getEncodedPKCS7(hash);
        if (contentEstimated + 2 < encodedSig.length) {
            String message = "The estimated size for the signature is smaller than the required one. Terminating request..";
            signLogger.log("ERROR", message);
            throw new CustomException(message);
        }
        byte[] paddedSig = new byte[contentEstimated];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        // Replace the contents
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        try {
            signatureAppearance.close(dic2);
        } catch (IOException ex) {
            String message = "PdfSignatureAppearance close error for signature input, type: IOException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        } catch (DocumentException ex) {
            String message = "PdfSignatureAppearance close error for signature input, type: DocumentException";
            signLogger.logError(message, ex);
            throw new CustomException(message, ex);
        }
