public class RestClient {
    Client client = null;

    public RestClient() {
        client = Client.create();
        //client.addFilter(new LoggingFilter(System.out));
    }

    public ClientResponse postData(String url, Map headerMap) throws IOException {
        ClientResponse response = null;

        try {
            WebResource webResource = client.resource(url);
            Builder requestBuilder = webResource.getRequestBuilder();
            Iterator it = headerMap.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                requestBuilder = requestBuilder.header(pair.getKey().toString(), pair.getValue());
            }

            if (headerMap.get("filename") != null) {
                ClassLoader cl = getClass().getClassLoader();
                InputStream fileInStream = cl.getResourceAsStream(headerMap.get("filename").toString());
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                requestBuilder = requestBuilder.entity(fileInStream);
            }

            response = requestBuilder.header("Expect", new String("100-continue"))
                    .post(ClientResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public ClientResponse getData(String url) {
        ClientResponse response = null;

        WebResource webResource = client.resource(url);
        response = webResource.get(ClientResponse.class);

        return response;
    }
}
