val lines = Source.fromFile(filePath, "UTF-8").getLines().toList
MessageDigest.getInstance("SHA-256")
.digest(lines.mkString("\n").getBytes).map("%02x".format(_)).mkString
