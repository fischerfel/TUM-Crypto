public class HttpHandler {
    public static SSLContext Certificate() {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        Certificate ca;

        AssetManager assetManager = Ctxt.getContext().getAssets();
        InputStream readcert = null;
        try {
            readcert = assetManager.open("server.crt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream caInput = new BufferedInputStream(readcert);
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            try {
                keyStore.load(null, null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyStore.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = null;
            try {
                tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            tmf.init(keyStore);
            context.init(null, tmf.getTrustManagers(), null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return context;
    }
    public static String HttpPost(String myurl, ArrayList<String[]> params) throws IOException {

        URL url;
        HttpsURLConnection conn;
        InputStream inStream = null;
        SSLContext ctxt = Certificate();
        try {
            url = new URL(myurl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ctxt.getSocketFactory());
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String query = buildUri(params);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            inStream = conn.getInputStream();
            return readIt(inStream);
        }finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
    private static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        return IOUtils.toString(stream, "UTF-8");
    }
    public static String buildUri(ArrayList<String[]> list) throws IOException {
        Uri.Builder builder = new Uri.Builder();
        for (int i = 0; i < list.size(); i++) {
            String[] entry = list.get(i);
            builder.appendQueryParameter(entry[0], entry[1]);
        }
        String query = builder.build().getEncodedQuery();
        return query;
    }
}
