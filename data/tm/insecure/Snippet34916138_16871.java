public class MainMethod {

public static void main(String[] args) {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};
            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                System.out.println(e);
            }
            ODataConsumer.Builder builder = ODataConsumers.newBuilder(QueryParams.SERVICEURL);
            builder.setClientBehaviors(new BasicAuthenticationBehavior(QueryParams.USER, QueryParams.PW));
            ODataConsumer consumer = builder.build();
            System.out.println(consumer);
            readEntities(consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
}

private static void readEntities(ODataConsumer consumer) throws Exception {
    try {
        Enumerable<OEntity> entity = consumer.getEntities("TestcaseSet").top(3).execute(); // .first();
        if (entity == null) {
            System.out.println("null-<<<<<");
        }
        Iterator<OEntity> entityLst = entity.iterator();
        while (entityLst.hasNext()) {
            List<OProperty<?>> properties = entityLst.next()
                    .getProperties();
            for (OProperty<?> oProp : properties) {
                System.out.println("Name " + oProp.getName() + "Type "
                        + oProp.getType() + "Value: " + oProp.getValue());
            }
        }
    } catch (RuntimeException runE) {
        System.out.println(runE);
        if ("No elements".equals(runE.getMessage()))
            throw new WrongPersonIdException();
    }
}
