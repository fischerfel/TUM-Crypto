private class LoginTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... input) {
        int count = input.length;
        if (count != 4)
            return false;
        String ipAddress = input[0];
        int portNumberInt = Integer.parseInt(input[1]);
        String username = input[2];
        String password = input[3];
        // Step 0: Establish a connection to the server
        PrintWriter out;
        BufferedReader in;
        try {
            serverSocket = new Socket(ipAddress, portNumberInt);
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        } catch (IOException e) {
            ((TextView)findViewById(R.id.textView1)).setText(e.getMessage());
            ((TextView)findViewById(R.id.textView2)).setText("");
            return false;
        }
        // Step 1: send "Authorize" to the server
        out.print("Authorize");
        // Step 2: server sends a random 64 character challenge string
        char[] buffer = new char[64];
        String challenge = null;
        try {
            in.read(buffer);
            challenge = new String(buffer);
        } catch (IOException e) {
            ((TextView)findViewById(R.id.textView1)).setText(e.getMessage());
            ((TextView)findViewById(R.id.textView2)).setText("");
            return false;
        }
        challenge = username + password + challenge;
        // Step 3: Compute MD5 hash of username + password + challenge and send it to the server
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        byte[] digest = md5.digest(challenge.getBytes());
        out.print(digest);
        // Step 4: Server computes the same hash, determines whether or not to accept the connection
        String authResult;
        try {
            in.read(buffer);
            authResult = new String(buffer);
        } catch (IOException e) {
            ((TextView)findViewById(R.id.textView1)).setText(e.getMessage());
            ((TextView)findViewById(R.id.textView2)).setText("");
            return false;
        }
        if (authResult.equals("Pass")) {
            return true;
        } else {
            return false;
        }
    }

}
