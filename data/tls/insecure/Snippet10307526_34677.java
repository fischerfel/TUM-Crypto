{
    final char[] CertPassphrase = "changeit".toCharArray(); 
    private boolean installCert() throws
                             KeyStoreException, NoSuchAlgorithmException,
                             CertificateException, KeyManagementException,
                             IOException {
    boolean isCertInstalled = true;
    File file = new File("jcacerts");
    if (!file.isFile())
    {
        char SEP = File.separatorChar;
        File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
        file = new File(dir, "jssecacerts");
        if (!file.isFile())
        {
            file = new File(dir, "cacerts");
        }
    }
    InputStream in = new FileInputStream(file);
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(in, CertPassphrase);
    in.close();
    SSLContext context = SSLContext.getInstance("TLS");
    TrustManagerFactory tmf =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
    SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
    context.init(null, new TrustManager[]
           {
               tm
           }, null);
    SSLSocketFactory factory = context.getSocketFactory();
    SSLSocket socket = (SSLSocket) factory.createSocket(this.getStrUrl(), this.getPort() );
    socket.setSoTimeout(10000);

    try
    {
        socket.startHandshake();
        socket.close();
    } catch (SSLException e)
    {
        isCertInstalled = false;
    }
    if(!isCertInstalled){
        X509Certificate[] chain = tm.chain;
        if (chain == null)
        {
           return true;
        }
        BufferedReader reader =
           new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Certificat from "+this.getStrUrl()+". [Enter] to download, [q] to quit!");
        String line = reader.readLine().trim();
        if(line.length()==0){
            for(int i=0; i<chain.length; i++){
                X509Certificate cert = chain[i];
                String alias = this.getStrUrl() + "-" + (i + 1);
                ks.setCertificateEntry(alias, cert);

                OutputStream out = new FileOutputStream("jssecacerts");
                ks.store(out, this.CertPassphrase);
                out.close();
                System.out.println("Added certificate to keystore 'jcacerts'");
            }
            return true;
        }else{
            return false;
        }
    }else
       return true;
}

private HttpsURLConnection openHttpsConnection(){
    try{
        if(this.installCert()){
            return (HttpsURLConnection)this.getUrl().openConnection();
        }
        else{
            System.out.println("Zertifikat heruntergeladen");
            return null;
        }
    }catch (KeyStoreException e){
         System.out.println(e.getMessage());
    }catch (NoSuchAlgorithmException e){
        System.out.println(e.getMessage());
    }catch (CertificateException e){
        System.out.println(e.getMessage());
    }catch (KeyManagementException e){
        System.out.println(e.getMessage());
    }catch (IOException e){
        System.out.println(e.getMessage());
    }
    return null;
}

private void closeHttpsConnection(HttpsURLConnection con){
    con.disconnect();
}

public void print_content(){
    HttpsURLConnection con = this.openHttpsConnection();
    try{
        BufferedReader br = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String input;
        while ((input = br.readLine()) != null){
            System.out.println(input);
        }
        br.close();
    } catch (IOException e) {
       e.printStackTrace();
    }
}

public Https(String Url)            { this.setUrl(Url); }

private static class SavingTrustManager implements X509TrustManager
{

   private final X509TrustManager tm;
   private X509Certificate[] chain;

   SavingTrustManager(X509TrustManager tm)
   {
       this.tm = tm;
   }

   public X509Certificate[] getAcceptedIssuers()
   {
       throw new UnsupportedOperationException();
   }

   public void checkClientTrusted(X509Certificate[] chain, String authType)
           throws CertificateException
   {
       throw new UnsupportedOperationException();
   }

   public void checkServerTrusted(X509Certificate[] chain, String authType)
           throws CertificateException
   {
       this.chain = chain;
       tm.checkServerTrusted(chain, authType);
   }
}
