String response = "";

URL url = new URL("https://localhost:9043/myservlet);

final SSLContext ctx = SSLContext.getInstance("TLSv1.2");
ctx.init(null, null, null);
// final String protoccol = ctx.getProtocol();

HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

conn.setReadTimeout(15000);
conn.setConnectTimeout(15000);
conn.setRequestMethod("POST");
conn.setDoInput(true);
conn.setDoOutput(true);

final OutputStream os = conn.getOutputStream();
final BufferedWriter writer =
  new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

writer.write(......);

writer.flush();
writer.close();
os.close();

final int responseCode = conn.getResponseCode();

if (responseCode == HttpsURLConnection.HTTP_OK) {
  String line;
  final BufferedReader br =
    new BufferedReader(new InputStreamReader(conn.getInputStream()));
  while ((line = br.readLine()) != null) {
    response += line;
  }
}

System.out.println("response: " + response);
