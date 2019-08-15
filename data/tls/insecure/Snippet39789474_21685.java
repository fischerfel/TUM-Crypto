        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, null, new SecureRandom());
        IO.setDefaultSSLContext(sc);
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
            public boolean verify(String hostname, SSLSession session){
                return true;
            }
        });
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        opts.secure = true;
        opts.sslContext = sc;

        this.socket = IO.socket(this.SERVER_URL, opts);
