URL u = new URL("https://foobar.de/");
final HttpsURLConnection openConnection = (HttpsURLConnection) u.openConnection();
openConnection.setHostnameVerifier(myVerifier);
u.openStream();// <!--EXCEPTION
