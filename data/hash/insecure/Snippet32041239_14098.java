String userName="WSADVINS" ; String password1 ='QEg0mep2fappUlJKKlA3B8K73g=="; String nonce1="o7wgBBqpzrDWOlSIBIHm7Q=="; String dateTime = "2007-02-20T19:45:56.456Z";
                MessageDigest sha1;
                  String passwordDigest=null;
                  try {
                    java.security.SecureRandom random = java.security.SecureRandom.getInstance("SHA1PRNG");
                    random.setSeed(System.currentTimeMillis()); 
                    byte[] nonceBytes = new byte[16]; 
                    random.nextBytes(nonceBytes); 
                    String nonce = new String(org.apache.commons.codec.binary.Base64.encodeBase64(nonceBytes), "UTF-8");
 sha1= MessageDigest.getInstance("SHA-1");
                      byte[] hash = MessageDigest.getInstance("SHA-1").digest(nonce.getBytes("UTF-8"));
                      sha1.update(dateTime.getBytes("UTF-8"));
                      sha1.update(password1.getBytes("UTF-8"));
                      passwordDigest = new String(Base64.encode(sha1.digest(hash)));
                      sha1.reset();
                  } catch (Exception e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                  }
            System.out.println("Generated password digest: " +passwordDigest );

              SOAPElement password = usernameToken.addChildElement("Password", "wsse");
            password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
              password.addTextNode(passwordDigest);
