//I declare the timestamp here
    Long tsLong = System.currentTimeMillis()/1000;
    final String ts = tsLong.toString();

//Create the string to be hashed
    final String toHash = ts + privateKey + publicKey;

    url = "" + publicKey;

    String hash = new String();
//my hashing algorithm
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashe = md.digest(toHash.getBytes("UTF-8"));
        StringBuffer hex = new StringBuffer(2*hashe.length);
        for (byte b : hashe) {
            hex.append(String.format("%02x", b&0xff));
        }
        hash = hex.toString();
    }
    catch(NoSuchAlgorithmException e) {

    }
    catch(UnsupportedEncodingException e) {

    }
//append the url to include the hash
    url += "&hash=";
    url += hash;

//call the api
    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

    JsonObjectRequest obj = new JsonObjectRequest
            (Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            parseJSONResponse(response);
            for (int i = 0; i < eventArray.size(); i++) {
                mTV.append("Id: " + String.valueOf(eventArray.get(i).id) + '\n' +
                        "Title: " + eventArray.get(i).title + '\n' +
                        "Description: " + eventArray.get(i).description + '\n');
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
//i get an authentication error so here is the log code
            Log.i("tag", url);
            Log.i("tag", toHash);

        }
    });

    queue.add(obj);
