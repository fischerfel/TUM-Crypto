SSLContext sslContext = null;

   try {
       sslContext = SSLContext.getInstance("SSL");
   } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NullPointerException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
