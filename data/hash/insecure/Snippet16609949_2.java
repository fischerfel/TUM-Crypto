MessageDigest pwdDigest=MessageDigest.getInstance("MD5");
pwdDigest.update(password.getBytes("UTF-16"));
byte rawbyte[]=pwdDigest.digest();
String passwordHash=Base64.encodeToString(rawbyte,Base64.DEFAULT);


URL url = new URL(loginURL);

HttpURLConnection Connection = (HttpURLConnection) url.openConnection();

Connection.setReadTimeout(10000);
Connection.setAllowUserInteraction(false);

Connection.setDoOutput(true);

//set the request to POST and send

Connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

DataOutputStream out = new DataOutputStream(Connection.getOutputStream());
out.writeBytes("username=" + URLEncoder.encode(username, "UTF-8"));
out.writeBytes("&password="+URLEncoder.encode(passwordHash,"UTF-8"));
out.flush();
out.close();
if(Connection.getResponseCode()==200){
  String data="Connected";            
  return data;
} else 
  return Connection.getResponseCode()+": "+Connection.getResponseMessage();
