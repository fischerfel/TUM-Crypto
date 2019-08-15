   import org.bouncycastle.cms.CMSAlgorithm;
    import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
    import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;

    Security.addProvider(new BouncyCastleProvider());            
    SMIMEEnvelopedGenerator encrypter = new SMIMEEnvelopedGenerator();
    InputStream inStream = new FileInputStream(cert_file);
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
     X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);


    MessageDigest dig = MessageDigest.getInstance("SHA1", "BC");
                dig.update(SubjectPublicKeyInfo.getInstance(cert.getPublicKey().getEncoded()).getPublicKeyData().getBytes());
 issue here --->   encrypter.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(dig.digest(),cert.getPublicKey()).setProvider("BC"));
         inStream.close();
    MimeMessage msg = new MimeMessage(mailSession);         
 issue here -->   MimeBodyPart encryptedPart = encrypter.generate(msg, new JceCMSContentEncryptorBuilder(CMSAlgorithm.DES_EDE3_CBC).setProvider("BC").build());
