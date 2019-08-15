public String returnBalances() {
    try {
        long nonce = System.nanoTime();
        String params = "command=returnBalances&nonce=" + nonce;

        URL u = new URL(URL_PRIVATE + "?" + params);

        String sign = getSign(params);

        if(sign == null) return null;

        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        huc.setRequestProperty("Key", API_Key);
        huc.setRequestProperty("Sign", sign);
        huc.setRequestProperty("nonce", String.valueOf(nonce));

        return getDataFromHUC(huc);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

private String getSign(String c) {
    try {
        SecretKeySpec mac_key = new SecretKeySpec(API_secret.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(mac_key);
        String sign = bytesToHex(mac.doFinal((c).getBytes()));
        return sign;
    }
    catch (Exception e) {
        return null;
    }
}
private String getDataFromHUC(HttpURLConnection huc) {
    try {
        huc.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        String data = sb.toString();

        return data;
    }
    catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
