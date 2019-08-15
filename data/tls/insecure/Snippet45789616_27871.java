    //url = new URL("http://172.20.1.80/password");
    //HttpURLConnection connection = setHttpConnect((HttpURLConnection) url.openConnection()); 
    url = new URL("https://172.20.1.80/password");
    HttpsURLConnection connection = setHttpsConnect((HttpsURLConnection) url.openConnection()); 
    sc = SSLContext.getInstance("TLS");
    sc.init(null, new TrustManager[] { new MyTrust() }, new java.security.SecureRandom());
    connection.setSSLSocketFactory(sc.getSocketFactory());

    connection.setDoInput(true);
    connection.setDoOutput(true);
    StringBuffer set = new StringBuffer();
    newpw = "123456"; 
    boundary = "----abc123abc1234-java";
    set.append(boundary + "\r\n").append("Content-Disposition: form-data; name=\"pw\"\r\n\r\n")
            .append(newpw).append("\r\n" + boundary + "--\r\n")
            .append("Content-Disposition: form-data; name=\"con_pw\"\r\n\r\n").append(newpw)
            .append("\r\n" + boundary + "--\r\n");
    out = new PrintWriter(connection.getOutputStream());
    out.write(set.toString());
    out.flush();

    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
    while ((str = in.readLine()) != null) {
        result += (str + "\n");
        if (str.indexOf(pwSet) != -1) {
            succ += 1;
            setPwSucc = true;
        }
    }
