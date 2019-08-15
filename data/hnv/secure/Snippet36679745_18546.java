    private void postText(String URLAddress) {
    try {
        URL obj = new URL(URLAddress);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setHostnameVerifier(DO_NOT_VERIFY);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
            con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            boolean First = true;
            while ((inputLine = in.readLine()) != null) {
                if(First)
                    First=false;
                else
                    response.append("\n");
                response.append(inputLine);
            }
            in.close();

            RequestResponse=response.toString();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
