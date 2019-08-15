public PHPDriver() {
  String pwd = "aabbccdd";
  String p = encodeByPHP("http://localhost/testsite/md5.php?pwd=" + pwd);
  System.out.println("PHPDriver:       " + pwd + " -> " + p);

  System.out.println("md5:             " + pwd + " -> " + md5(p));
...
public String encodeByPHP(String url) {
  try {
    // create a link to  a URL
    URL urlAddress = new URL(url);
    URLConnection link = urlAddress.openConnection();
    BufferedReader inStream = new BufferedReader(new InputStreamReader(link.getInputStream()));
    return inStream.readLine();  
  } catch (MalformedURLException e) {
...
public String md5(String input)  {
  String result = input;
  try {
    if(input != null) {
      MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
      md.update(input.getBytes());
      BigInteger hash = new BigInteger(1, md.digest());
      result = hash.toString(16);
      while(result.length() < 32) {
        result = "0" + result;
      }
    }
  } catch (NoSuchAlgorithmException nsa) {
