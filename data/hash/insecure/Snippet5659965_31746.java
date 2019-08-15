        TimeStampRequestGenerator reqgen = new TimeStampRequestGenerator();
        reqgen.setCertReq(true);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update("messageImprint".getBytes());
        byte[] digest = messageDigest.digest();
        String ocspUrl = "http://egtstamp.egroup.hu:80/tsa";
        OutputStream out = null;

        TimeStampRequest req = reqgen.generate(TSPAlgorithms.SHA1, digest); 
        byte request[] = req.getEncoded(); 

        URL url = new URL(ocspUrl); 
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); 

        con.setDoOutput(true); 
        con.setDoInput(true); 
        con.setRequestMethod("POST"); 
        con.setRequestProperty("Content-type", "application/timestamp-query"); 

        con.setRequestProperty("Content-length", String.valueOf(request.length)); 
        out = con.getOutputStream(); 
        out.write(request); 
        out.flush(); 
