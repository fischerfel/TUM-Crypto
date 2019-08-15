    String api_key = // your api key
    String api_sig = // your api sig
    String username = // username you want to log in
    String password = // user password

    String apiSignature = "api_key" + api_key + "methodauth.getMobileSessionpassword" + password + "username" + username + api_sig;

    StringBuilder hexString = new StringBuilder();

    try {
        MessageDigest md5Encrypter = MessageDigest.getInstance("MD5");
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest
                .getInstance("MD5");
        digest.update(apiSignature.getBytes("UTF-8"));
        byte messageDigest[] = digest.digest();

        // Create Hex String
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }

        String urlParameter = "method=auth.getMobileSession&api_key=" + api_key + "&password=" + password + "&username=" + username + "&api_sig=" + hexString;
        Request request = new Request.Builder()
                .url("https://ws.audioscrobbler.com/2.0/?" + urlParameter).post(RequestBody.create(null, new byte[0])).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String test = responseBody.string(); // your .xml with the session id, see https://www.last.fm/api/show/auth.getSession
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                }
            }
        });
    } catch (Exception ex) {
        System.out.println(ex.toString());
    }
