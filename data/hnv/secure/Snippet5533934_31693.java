public static void main(String args[]) {

    final String username = "user";
    final String password = "password";



    Authenticator.setDefault(new Authenticator() {
        @Override
          protected PasswordAuthentication getPasswordAuthentication() {
                PasswordAuthentication pa = new PasswordAuthentication (username, password.toCharArray());
                //System.out.println(pa.getUserName() + ":" + new String(pa.getPassword()));
                return pa;
            }
          });
    BufferedReader in = null;
    StringBuffer sb = new StringBuffer();

    try {
        //URL url = new URL(strURL);

        HttpsURLConnection connection = (HttpsURLConnection) new URL("https://secureHost/").openConnection();
                    connection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
                    connection.setHostnameVerifier(new CustomizedHostnameVerifier());
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type","text/xml");
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    String requestString = "<request method=\"switchvox.currentCalls.getList\"></request>";

                    out.println(requestString);
                    out.close();

        in = new BufferedReader(new InputStreamReader(connection
                .getInputStream()));

        String line;

        while ((line = in.readLine()) != null) {
            sb.append(line).append("\n");
        }
    } catch (java.net.ProtocolException e) {
        sb.append("User Or Password is wrong!");
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    System.out.println("The Data is: " + sb.toString());

}
