HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
https.setHostnameVerifier(DO_NOT_VERIFY);
http = https;
http.setRequestMethod("POST");
http.setDoInput(true);
http.setDoOutput(true);
