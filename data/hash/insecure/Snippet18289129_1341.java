val (key, secret, serviceURL) = 
  (properties("ESHQ_KEY"), properties("ESHQ_SECRET"), properties("ESHQ_URL"))

def createSocket(channel: String) = post("/socket", Map("channel" -> channel))

def publish(data: String, channel: String) = post("/event", 
  Map("channel" -> channel, "data" -> data))

private def post(path: String, params: Map[String, String]): ActionResult = {
  val request = url(s"$serviceURL$path") << params << credentials
  Await.result(Http(request).either, 5.seconds).fold(
    {t => InternalServerError(s"Failed to post $request: ${t.getMessage}")},
    {r => Ok(r.getResponseBody)}
  )
}

private def credentials = {
  val time = (System.currentTimeMillis / 1000).toString
  Map("key" -> key, "timestamp" -> time, "token" -> token(key, secret, time))
}

private def token(strings: String*) = {
  val md = java.security.MessageDigest.getInstance("SHA-1")
  md.digest(strings.mkString(":").getBytes("UTF-8")).map("%02x".format(_)).mkString
}
