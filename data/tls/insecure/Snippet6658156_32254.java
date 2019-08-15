// CREATE EPHEMERAL KEYSTORE FOR THIS SOCKET USING THE DESIRED CERTIFICATE
try {
    final char[]      BLANK_PWD=new char[0];
    SSLContext        ctx=SSLContext.getInstance("TLS");
    KeyManagerFactory kmf=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    Key               ctfkey=mstkst.getKey(svrctfals,BLANK_PWD);
    Certificate[]     ctfchn=mstkst.getCertificateChain(svrctfals);
    KeyStore          sktkst;

    sktkst=KeyStore.getInstance("jks");
    sktkst.load(null,BLANK_PWD);
    sktkst.setKeyEntry(svrctfals,ctfkey,BLANK_PWD,ctfchn);
    kmf.init(sktkst,BLANK_PWD);
    ctx.init(kmf.getKeyManagers(),null,null);
    ssf=ctx.getServerSocketFactory();
    }
catch(java.security.GeneralSecurityException thr) {
    throw new IOException("Cannot create server socket factory using ephemeral keystore ("+thr+")",thr);
    }
