Scanner scanner = new Scanner(inputStream,"UTF-8");
String data = scanner.useDelimiter("\\r\\n\\r\\n").next(); 
Matcher get = Pattern.compile("^GET").matcher(data);
if (get.find()) {
    Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
    match.find();
    String clientKey =  (match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
    String digest =  DatatypeConverter.printBase64Binary(MessageDigest.getInstance("SHA-1").digest(clientKey.getBytes("UTF-8")));
    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Sec-WebSocket-Accept: " + digest
                        + "\r\n\r\n")
                        .getBytes("UTF-8");
     outputStream.write(response, 0, response.length);
     outputStream.flush();


     scanner.useDelimiter(""); //then, reading all input data
     System.out.println("input stream: ");
     while (scanner.hasNext())
     {
         System.out.print(scanner.next());
     }
     System.out.println("input done");
}
