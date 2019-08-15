MailcapCommandMap mailcap = (MailcapCommandMap)CommandMap
                .getDefaultCommandMap();

        mailcap
                .addMailcap("application/pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
        mailcap
                .addMailcap("application/pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
        mailcap
                .addMailcap("application/x-pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
        mailcap
                .addMailcap("application/x-pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
        mailcap
                .addMailcap("multipart/signed;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");

        CommandMap.setDefaultCommandMap(mailcap);
        /*
         * FIN MAILCAP AGREGADO
         */

        args = new String[6];
        args[0] = "ITtest.p12";
        args[1] = "pass";
        args[2] = "1.1.1.example";
        args[3] = "ManuelRodriguez.cer"; //certificado
        args[4] = "noreply_sistemas@siman.com"; //from
        args[5] = "daniel_hernandez@siman.com,omar_rodriguez@siman.com"; //to
        //args[5] = "daniel_hernandez@siman.com,omar_rodriguez@siman.com, manuel_rodriguez@siman.com"; //to

        //
        // Open the key store
        //      
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
        ks.load(Pivote.class.getResourceAsStream("ITtest.p12"), "pass".toCharArray());

        Enumeration e = ks.aliases();
        String      keyAlias = null;

        while (e.hasMoreElements())
        {
            String  alias = (String)e.nextElement();

            if (ks.isKeyEntry(alias))
            {
                keyAlias = alias;
            }
        }

        if (keyAlias == null)
        {
            System.err.println("can't find a private key!");
            System.exit(0);
        }

        Certificate[]   chain = ks.getCertificateChain(keyAlias);



        /*
         * INSTANCIAMOS EL CERTIFICADO DE IVAN
         */        
        InputStream fr =  Pivote.class.getResourceAsStream(args[3]);
        CertificateFactory cf =  CertificateFactory.getInstance("X509");
        X509Certificate crt = (X509Certificate) cf.generateCertificate(fr);

        /*
         * 
         */

        //
        // create the generator for creating an smime/encrypted message
        //
        SMIMEEnvelopedGenerator  gen = new SMIMEEnvelopedGenerator();          
        gen.addKeyTransRecipient(crt);
        //gen.addKeyTransRecipient((X509Certificate)chain[0]);



        //
        // create a subject key id - this has to be done the same way as
        // it is done in the certificate associated with the private key
        // version 3 only.
        //
        /*
        MessageDigest           dig = MessageDigest.getInstance("SHA1", "BC");

        dig.update(cert.getPublicKey().getEncoded());

        gen.addKeyTransRecipient(cert.getPublicKey(), dig.digest());
        */

        //
        // create the base for our message
        //
        MimeBodyPart    msg = new MimeBodyPart();

        msg.setText("¿PUEDEN VER ESTA PARTE DEL MENSAJE?");

        MimeBodyPart mp = gen.generate(msg, SMIMEEnvelopedGenerator.RC2_CBC, "BC");
        //
        // Get a Session object and create the mail message
        //
        Properties props = System.getProperties();
        props.put("mail.smtp.host", args[2]);
        Session session = Session.getDefaultInstance(props, null);

        Address fromUser = new InternetAddress(args[4]);
        //Address toUser = new InternetAddress(args[5], false);

        MimeMessage body = new MimeMessage(session);
        body.setFrom(fromUser);
        //body.setRecipient(Message.RecipientType.TO, toUser);
        body.setRecipients(Message.RecipientType.TO, args[5]);
        body.setSubject("Confirmar si ven mensaje, por favor " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        body.setContent(mp.getContent(), mp.getContentType());
        body.saveChanges();
        body.writeTo(new FileOutputStream("encrypted.message"));

        /*
         * Firmo el mensaje
         */
        EncryptionUtils smimeUtils = EncryptionManager.getEncryptionUtils(EncryptionManager.SMIME);    
        EncryptionKeyManager smimeKeyMgr = smimeUtils.createKeyManager();
        smimeKeyMgr.loadPrivateKeystore(Pivote.class.getResourceAsStream("ITtest.p12"), "pass".toCharArray());
        Key privateKey = smimeKeyMgr.getPrivateKey((String)smimeKeyMgr.privateKeyAliases().iterator().next(), "pass".toCharArray());
        smimeUtils.signMessage(session, body, privateKey);
        /*
         * Fin de firja
         */


        Transport.send(body);

        System.out.println("Mensaje enviado");
