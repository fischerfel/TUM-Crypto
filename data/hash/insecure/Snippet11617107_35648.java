for(int i=0;i<2;i++) {
    String pkcs11ConfigSettings =
                "name = AuthProvider\nlibrary = C:\\Program Files\\ActivIdentity\\ActivClient\\acpkcs201-ns.dll";
    AuthProvider p = (SunPKCS11)Security.getProvider("SunPKCS11-AuthProvider");
    if(p==null) {
        p = new SunPKCS11(new ByteArrayInputStream(pkcs11ConfigSettings.getBytes()));
        p.setCallbackHandler(new CallbackHandler() {    
            @Override
            public void handle(Callback[] callbacks) throws IOException,
                    UnsupportedCallbackException {
                for(Callback c : callbacks)
                    if(c instanceof PasswordCallback) {
                        //HACK. if we set password to null it will bring up the drivers PIN dialog.
                        ((PasswordCallback) c).setPassword(null);
                    }
            }
        });
        Security.addProvider(p);
    }
    KeyStore ks = KeyStore.getInstance("PKCS11",p); 
    ks.load(null, null);
    Certificate[] cc = ks.getCertificateChain("Digital Signature Key");
    PrivateKey pk = (PrivateKey)ks.getKey("Digital Signature Key", null);
    OutputStream fos = new FileOutputStream("c:\\doc" + i + ".pdf"); ;
    PdfReader reader = new PdfReader(new FileInputStream(new File("C:\\1.pdf")));
    PdfStamper stamper = PdfStamper.createSignature(reader, fos, '\0');
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
    PdfPKCS7 pk7 = new PdfPKCS7(pk, cc, null, "SHA1", "SunPKCS11-AuthProvider", false);
    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
    byte buf[] = new byte[8192];
    int n;
    InputStream inp = sap.getRangeStream();
    while ((n = inp.read(buf)) > 0) {
        messageDigest.update(buf, 0, n);
    }
    byte hash[] = messageDigest.digest();
    byte sh[] = pk7.getAuthenticatedAttributeBytes(hash, cal, null);
    pk7.update(sh, 0, sh.length);
    PdfDictionary dic2 = new PdfDictionary();
    byte sg[] = pk7.getEncodedPKCS7(hash, cal);
    byte out[] = new byte[0x2500 / 2];
    System.arraycopy(sg, 0, out, 0, sg.length);
    dic2.put(PdfName.CONTENTS, new PdfString(out).setHexWriting(true));
    sap.close(dic2);
}
