public class Test{
    public static void main(String[] args) {
        String[] lines = null;

        try {
             // configure the SSLContext with a TrustManager
            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            } catch (KeyManagementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SSLContext.setDefault(ctx);

            String url = "https://www.google.com"

            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.getElementById("table_UniqueReportID").children();
            for(Element element : elements)
            {
                System.out.println(element.nodeName());
                if(element.nodeName().equalsIgnoreCase("tbody"))
                {
                    Elements rowElements = element.children();
                    for(Element currentRow : rowElements)
                    {
                        System.out.println(currentRow.text());
                    }
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static class DefaultTrustManager implements X509TrustManager {


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }
    }
}
