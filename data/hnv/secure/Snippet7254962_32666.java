public static String getmyproductquestiondetails(String userid,
        String campaignid) {// https://www.buzzador.com/apps/present_software/webservice/index.php?op=EducationResult&userid=1&questionid=1,2,3&answergivenbyuser=1,1,0
    String data = null;
    try {

        URL url = new URL(
                "http://dignizant.com/buzz/webservice/index.php?op=getProductQuestion&userid="
                        + userid + "&campaign_id=" + campaignid);
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url
                    .openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            http = https;
        } else {
            http = (HttpURLConnection) url.openConnection();
        }
    } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    Utils utils = new Utils();

    try {
        data = utils.convertStreamToString(http.getInputStream());
        System.out.println("getproduct details response :: " + data);

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

        data = e.toString();

    }
    return data;
}







try {


        JSONObject jo = new JSONObject(response);

    } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
    }
