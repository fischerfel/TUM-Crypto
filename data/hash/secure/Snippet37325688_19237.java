byte [] alteredPDF=output.toByteArray();


    ExternalDigest externalDigest = new ExternalDigest() {
        @Override
        public MessageDigest getMessageDigest(String hashAlgorithm) throws GeneralSecurityException {
            return DigestAlgorithms.getMessageDigest(hashAlgorithm, "BC");
        }
    };
    PdfSignatureAppearance sapFinal = null;
    try {
        ByteArrayOutputStream outputFinal = new ByteArrayOutputStream();
        pdfReader = new PdfReader(new ByteArrayInputStream(alteredPDF));
        pdfStamper = PdfStamper.createSignature(pdfReader, outputFinal, '\0');
        sapFinal = pdfStamper.getSignatureAppearance();
        sapFinal.setVisibleSignature("sig");
        sapFinal.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
        sapFinal.setCertificate(cert);
        sapFinal.setSignDate(cal);

        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        dic.setReason(sap.getReason());
        dic.setLocation(sap.getLocation());
        String certInfo = cert.getSubjectX500Principal().getName();
        dic.setName(certInfo.substring(certInfo.indexOf("CN=") + 3,
                certInfo.indexOf(",OU", certInfo.indexOf("CN=") + 3)));
        dic.setSignatureCreator(sap.getSignatureCreator());
        dic.setContact(sap.getContact());
        dic.setCert(certDecoded);
        dic.setDate(new PdfDate(sap.getSignDate()));
        sapFinal.setCryptoDictionary(dic);

        HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
        exc.put(PdfName.CONTENTS, new Integer(estimatedSize * 2 + 2));
        sapFinal.preClose(exc);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (DocumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }catch(Exception e){
        e.printStackTrace();
    }


    byte[] sh = null;
    byte[] hashVal = null;
    PdfPKCS7 sgn = null;



    try {
        sgn = new PdfPKCS7(null, new Certificate[] { cert }, "SHA256", null, externalDigest, false);

        InputStream data = sapFinal.getRangeStream();
        hashVal = DigestAlgorithms.digest(data, externalDigest.getMessageDigest("SHA256"));
        sh = sgn.getAuthenticatedAttributeBytes(hashVal, cal, null, null, CryptoStandard.CMS);
        sh = MessageDigest.getInstance("SHA256", "BC").digest(sh);

    } catch (IOException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (GeneralSecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
