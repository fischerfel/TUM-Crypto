Mac hmac = Mac.getInstance("HmacSHA256");
SecretKeySpec secret_key = new SecretKeySpec(Charset.forName("UTF-8").encode(this.secret).array(), "HmacSHA256");
hmac.init(secret_key);
byte[] digest = hmac.doFinal(this.secret.getBytes("UTF-8"));
System.out.println(hexify(digest));
System.out.println(new String(digest,"UTF-8"));
