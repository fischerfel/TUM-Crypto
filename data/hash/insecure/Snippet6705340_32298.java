String handshake;

handshake = "HTTP/1.1 101 WebSocket Protocol Handshake\n";
handshake += "Upgrade: "+handshakeUpgrade+"\n"; // handshakeUpgrade and the following variables are instance variables I set when I process the request
handshake += "Connection: "+handshakeConnection+"\n";
handshake += "Sec-WebSocket-Origin: "+handshakeOrigin+"\n";
handshake += "Sec-WebSocket-Location: "+handshakeOrigin.replace("http", "ws")+handshakeGetLocation+"\n";
handshake += "Sec-WebSocket-Protocol: sample\n";
// handshake += "\n";

String nums = calculateKeyReply(handshakeSecWebSocketKey1)+""+calculateKeyReply(handshakeSecWebSocketKey2);

MessageDigest md5Digestor = MessageDigest.getInstance("MD5");
String md5 = new String(md5Digestor.digest(nums.getBytes()));

handshake += md5;

return handshake;
