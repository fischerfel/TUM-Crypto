    int BUFFER_SIZE = 4096;
    String name = "file.txt";
    String method = "PUT";
    String bucket = "bucket";
    String secretKey = "******";
    String filePath = "F:\\file.txt";
    SimpleDateFormat df = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
    Date date = new Date();
    String formattedDate = df.format(date);
    File uploadFile = new File(filePath);


    URL url = new URL("http://bucket.s3.amazonaws.com/" + name);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    String resource = "/" + bucket + "/" + name;
    String contentType = "text/plain";
    String signn = method + "\n\n" + contentType + "\n" + formattedDate + "\n" + resource ;
    Mac hmac = Mac.getInstance("HmacSHA1");
    hmac.init(new SecretKeySpec(
            secretKey.getBytes("UTF-8"), "HmacSHA1"));
    String signature = (new BASE64Encoder()).encode(
            hmac.doFinal(signn.getBytes("UTF-8"))).replaceAll("\n", "");

    String authAWS = "AWS " + "**SECRET**" + ":" + signature;
    httpConn.setDoOutput(true);
    httpConn.setRequestMethod(method);
    httpConn.setRequestProperty("Accept", "*/*");
    httpConn.setRequestProperty("Date", formattedDate);
    httpConn.setRequestProperty("Content-type", contentType);
    httpConn.setRequestProperty("Authorization", authAWS);


    httpConn.setRequestProperty("x-amz-acl", "public-read");  // <----- THIS LINE HERE!


    OutputStream outputStream = httpConn.getOutputStream();
    FileInputStream inputStream = new FileInputStream(uploadFile);
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = -1;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
    }
    outputStream.close();
    inputStream.close();
    System.out.println("Response message : " + httpConn.getResponseMessage());
