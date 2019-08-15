MessageDigest cript = MessageDigest.getInstance("SHA-1");
              cript.reset();
              cript.update(userPass.getBytes("utf8"));
              this.password = new String(cript.digest());
