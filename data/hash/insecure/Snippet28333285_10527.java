val md = java.security.MessageDigest.getInstance("SHA-1")
val str = """ "7080001237543" + "1105" + "7080001237543" + "2015-02-04 12:23:55" + "site" + "0032014143" + "2" + "hJSbGEx5M7" """
val ha = new sun.misc.BASE64Encoder().encode(md.digest(str.getBytes))
