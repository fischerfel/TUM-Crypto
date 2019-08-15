    HttpURLConnection http = null;

    if (url.getProtocol().toLowerCase().equals("https")) {
        trustAllHosts();
        HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
        https.setHostnameVerifier(DO_NOT_VERIFY);
        http = https;
    } else {
        http = (HttpURLConnection) url.openConnection();
    }
