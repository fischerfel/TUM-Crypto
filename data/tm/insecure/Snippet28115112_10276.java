public class ClusterGeckoMeter {
static Geckoboard geckoboard = new Geckoboard("sfsfsdfds");
static GeckOMeter text;
static GeckOMeter textprod;

static {
    text = new GeckOMeter("wewrwer", GraphType.STANDARD);
    textprod = new GeckOMeter("sdsdfaf", GraphType.STANDARD);
}

public ClusterGeckoMeter() {
}

public static void main(String[] args) throws ValidationException, GeckoboardException, IOException, NoSuchAlgorithmException {
    handShake();
    pushToDevProd();
}

public static void handShake() throws IOException, NoSuchAlgorithmException {
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};
    SSLContext sc = SSLContext.getInstance("SSL");

    try {
        sc.init((KeyManager[])null, trustAllCerts, new SecureRandom());
    } catch (KeyManagementException var7) {
        var7.printStackTrace();
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    URL url = new URL("https://sri.geckoboard.com");
    URLConnection con = url.openConnection();
    InputStreamReader reader = new InputStreamReader(con.getInputStream());

    while(true) {
        int ch = reader.read();
        if(ch == -1) {
            return;
        }

        System.out.print((char)ch);
    }
}

public static void pushToDevProd() throws IOException {
    text.setCurrent("4");
    text.setMin("", "0");
    text.setMax("", "10");
    geckoboard.push(text);
    text.setCurrent("4");
    textprod.setCurrent("4");
    textprod.setMin("", "0");
    textprod.setMax("", "10");
    geckoboard.push(textprod);
}
