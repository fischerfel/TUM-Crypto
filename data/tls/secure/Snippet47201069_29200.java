 List<Protocol> protocols = new ArrayList<>();
            protocols.add(Protocol.HTTP_2);
            protocols.add(Protocol.HTTP_1_1);



            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, null, null);

            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);
            specs.add(ConnectionSpec.CLEARTEXT);

            mClient = new OkHttpClient.Builder()
                    .protocols(protocols)
                    .connectionSpecs(specs)
                    .sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()))
                    .build();
