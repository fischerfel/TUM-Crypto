public String sendLoginConnection(String data){

        String hexdigest = md5(data + ":pr3m1umWat3r154i:12").toUpperCase();
        return Connection("/apoints.php?fpts=12&version=a1.54&udid=" + data + "&pf=" + hexdigest + "&model=Droid&sv=2.2");
    }


    public String Connection(String page){
        try {
            String address = "http://im.storm8.com" + page;
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Droid; U; CPU iPhone OS 2_2 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Mobile/7D11");
            conn.setRequestProperty("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            conn.setRequestProperty("Accept-Language", "en-us");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.connect();

            GZIPInputStream gzipin = new GZIPInputStream(conn.getInputStream());
            byte[] buff = new byte[1024];
            byte[] emptyBuff = new byte[1024];
                                        StringBuffer unGzipRes = new StringBuffer();

                                        int byteCount = 0;
                                        while ((byteCount = gzipin.read(buff, 0, 1024)) > 0) {

                                            unGzipRes.append(new String(Arrays.copyOf(buff, byteCount), "utf-8"));


                                            System.arraycopy(emptyBuff, 0, buff, 0, 1024);
                                        }


                                        String content = unGzipRes.toString();
                                        return content;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
