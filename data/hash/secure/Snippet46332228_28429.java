MessageDigest.getInstance("SHA-256")
  .digest("some string".getBytes("UTF-8"))
  .map("%02x".format(_)).mkString
