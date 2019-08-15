KeyStore ks=KeyStore.getInstance("pkcs12");
ks.load(new FileInputStream("client_t_c1.p12"),"c1".toCharArray());

KeyStore jks=KeyStore.getInstance("JKS");
jks.load(null);

for (Enumeration<String>t=ks.aliases();t.hasMoreElements();)
{
    String alias = t.nextElement();
    System.out.println("@:" + alias);
    if (ks.isKeyEntry(alias)){
        Certificate[] a = ks.getCertificateChain(alias);
        for (int i=0;i<a.length;i++)
        {
            X509Certificate x509 = (X509Certificate)a[i];
            System.out.println(x509.getSubjectDN().toString());
            if (i>0)
                jks.setCertificateEntry(x509.getSubjectDN().toString(), x509);
            System.out.println(ks.getCertificateAlias(x509));
            System.out.println("ok");
        }
    }
}

System.out.println("init Stores...");

KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "c1".toCharArray());

TrustManagerFactory tmf=TrustManagerFactory.getInstance("SunX509");
tmf.init(jks);

SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
