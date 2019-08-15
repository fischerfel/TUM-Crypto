MessageDigest md = MessageDigest.getInstance("SHA-1");                   
                    md.update(sPassword.getBytes("UTF-8"));
                    byte[] res = md.digest();                    
                    return "sha1:" + StringUtils.byte2hex(res);
