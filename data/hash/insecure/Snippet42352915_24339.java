    String request = null;
    String result = "";
    String ApiId = "4d53bce03ec34c0a911";
    String ApiKey = "A93reRTUJHsCuQSHR+L3GxqOJ";
    String json = "";//"{\"key\":1}";
    json = json + "{Password :'Test@123' }";

    URL url;
    String requestContentBase64String = "";
    HttpURLConnection urlConnection = null;
    try {
        //url = new URL("http://mac3uat.jetprivilege.com/api/m1/v1/");
        url = new URL("http://localhost:43326/api/m1/v1/EnrolAccount/IsPasswordValid");

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(5000);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        //conn.setRequestMethod("POST");


        Date epochStart = new Date(0);

        //String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);
        String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);
        //String requestTimeStamp = "1487513216";

        String nonce = java.util.UUID.randomUUID().toString().replace("-", "");

        if (json != null) {
            byte[] content = json.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] requestContentHash = md.digest(content);
            requestContentBase64String = Base64.getUrlEncoder().encodeToString(requestContentHash);
        }
