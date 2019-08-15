public class XMLParser extends AsyncTask<Void, Void, ArrayList<Object>> {

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();
    ArrayList<String> child = new ArrayList<String>();


    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            System.out.println("authType is " + authType);
            System.out.println("cert issuers");
            for (int i = 0; i < certs.length; i++) {
                System.out.println("\t"
                        + certs[i].getIssuerX500Principal().getName());
                System.out.println("\t" + certs[i].getIssuerDN().getName());
            }
        }
    } };

    @Override
    protected ArrayList<Object> doInBackground(Void... params) {

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            URL url = new URL();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Device");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element deviceElement = (Element) nodeList.item(i);
                groupItem.add(deviceElement.getAttribute("serial"));
                child = new ArrayList<String>();
                child.add(deviceElement.getAttribute("model"));
                child.add(deviceElement.getAttribute("asset"));
                child.add(deviceElement.getAttribute("location"));
                child.add(deviceElement.getAttribute("lastConnected"));
                childItem.add(child);
            }
;
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }
        return childItem;
    }

    protected void onPostExecute() {
        // here you will get the result
    }

    public ArrayList<String> getGroupItem() {

        return groupItem;
    }

    public ArrayList<Object> getChildItem() {
        return childItem;
    }
