    try {
            com.sun.net.ssl.SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }


        try {
            System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
