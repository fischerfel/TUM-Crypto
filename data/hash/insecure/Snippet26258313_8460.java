public void signPDF(X509Certificate cert, List<File> fInl, List<File> fOutl,
                    PrivateKey key)
            throws IOException, NoSuchAlgorithmException, 
                   NoSuchProviderException, InvalidParameterSpecException,
                   DocumentException, InvalidKeyException,
                   SignatureException, KeyStoreException,
                   CertificateException, UnrecoverableKeyException {

    Certificate[] cc = new Certificate []{cert};

    int i=0;

    for(i=0;i<fInl.size();i++){
        PdfReader reader = new PdfReader(fInl.get(i).getAbsolutePath());
        FileOutputStream outputFile = new FileOutputStream(fOutl.get(i).getAbsolutePath());
        PdfStamper stamper = PdfStamper.createSignature(reader, outputFile, '\0');
        PdfSignatureAppearance sap = stamper.getSignatureAppearance();
        sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);

        Calendar cal = Calendar.getInstance();
        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        dic.setDate(new PdfDate(cal));
        dic.setName(PdfPKCS7.getSubjectFields((X509Certificate)cc[0]).getField("CN"));
        sap.setCryptoDictionary(dic);
        sap.setLayer2Text("Digitally signed by "+ dic.get(PdfName.NAME) +"\n\nDate: " + cal.getTime().toString());

        HashMap<PdfName,Object> exc = new HashMap<PdfName,Object>();
        exc.put(PdfName.CONTENTS, new Integer(0x2502));
        sap.preClose(exc);

        PdfPKCS7 pk7 = new PdfPKCS7(key, cc, null, "SHA1", null, false);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte buf[] = new byte[8192];
        int n;
        InputStream inp = sap.getRangeStream();

        while ((n = inp.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }

        byte hash[] = messageDigest.digest();
        byte sh[] = pk7.getAuthenticatedAttributeBytes(hash, cal);
        pk7.update(sh, 0, sh.length);

        PdfDictionary dic2 = new PdfDictionary();
        byte sg[] = pk7.getEncodedPKCS7(hash, cal);
        byte out[] = new byte[0x2500 / 2];

        System.arraycopy(sg, 0, out, 0, sg.length);
        dic2.put(PdfName.CONTENTS, new PdfString(out).setHexWriting(true));
        sap.close(dic2);
    }
}
