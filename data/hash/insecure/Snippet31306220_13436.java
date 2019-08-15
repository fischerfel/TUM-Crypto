    String tsaUrl = "http://ts.cartaodecidadao.pt/tsa/server";
    OutputStream out = null;
    HttpURLConnection con = null;
    MessageDigest hash;
    InputStream is = null;
    try{
        hash = MessageDigest.getInstance("SHA1");
        try{
           is = new BufferedInputStream(new FileInputStream(file));
           final byte[] buffer = new byte[1024];
           for (int i = 0; is.read(buffer) != -1;){
                hash.update(buffer, 0, i);
           }
         } catch (FileNotFoundException e){
                e.printStackTrace();
            }                
            TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();

            /*FIXME */
            ASN1ObjectIdentifier algorithm = TSPAlgorithms.SHA1;
            TimeStampRequest timeStampRequest = timeStampRequestGenerator.generate(algorithm, hash.digest());
            byte request[] = timeStampRequest.getEncoded();

            URL url = new URL(tsaUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type",
                    "application/timestamp-query");
            con.setRequestProperty("Content-length",
                    String.valueOf(request.length));
            out = con.getOutputStream();
            out.write(request);
            out.flush();
            out.close();

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                System.out.println("---------- != HttpURLConnection.HTTP_OK");
                throw new IOException("Received HTTP error: "
                        + con.getResponseCode() + " - "
                        + con.getResponseMessage());
            }

            InputStream in = con.getInputStream();
            TimeStampResp resp = TimeStampResp
                    .getInstance(new ASN1InputStream(in).readObject());
            TimeStampResponse response = new TimeStampResponse(resp);
