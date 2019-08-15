protected String doInBackground(Object... params) {

    try {           

        URL url = new URL("ValidURL");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Log.i("URL", "VERIFIER CREATED");

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("PUT");

        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        Log.i("STREAM", "STREAMS CREATED");            

        //Receive CA certificate + challenge
        ChallengeMessage challenge = (ChallengeMessage) in.readObject();
        certificateCA = challenge.getCertificate();
        byte[] nonce = challenge.getNonce();
        //Log.i("Nonce", nonce.toString());

        //Hash challenge and send back with user certificate
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedNonce = md.digest(nonce);
        //Log.i("Digest", hashedNonce.toString());

        ChallengeMessage challengeResponse = new ChallengeMessage(hashedNonce, (X509Certificate)params[1]);           
        out.writeObject(challengeResponse); //Here I get the StreamCorruptedException
        out.flush();            

        String mResp = (String) in.readObject();
        Log.i("ANSWER",mResp);            

        in.close();
        out.close();
        connection.disconnect();

        return mResp;

    } catch(ProtocolException e) {
        Log.i("VERIFIER", e.toString());
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();        
    }

    return "False";
}
