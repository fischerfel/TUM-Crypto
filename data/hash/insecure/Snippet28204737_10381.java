MessageDigest md = MessageDigest.getInstance("SHA1");
md.update(requestBody.getBytes());
byte[] output = Base64.encodeBase64(md.digest());
String hash = new String(output);
HttpParameters parameters = new HttpParameters();
parameters.put("oauth_body_hash", URLEncoder.encode(hash, "UTF-8"));

CommonsHttpOAuthConsumer signer = new CommonsHttpOAuthConsumer(key, secret);
HttpPost request = new HttpPost(url);
request.setHeader("Content-Type", "application/xml");
request.setEntity(new StringEntity(requestBody, "UTF-8"));
signer.setAdditionalParameters(parameters);
signer.sign(request);
