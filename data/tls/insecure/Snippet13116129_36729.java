if (method == "POST") {

            // Load the self-signed server certificate
            char[] passphrase = "ssltest".toCharArray();
            KeyStore ksTrust = KeyStore.getInstance("BKS");
            ksTrust.load(context.getResources().openRawResource(
                    R.raw.ssltestcert), passphrase);
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(ksTrust);

            // Create a SSLContext with the certificate
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(),
                    new SecureRandom());

            // request method is POST
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        }
