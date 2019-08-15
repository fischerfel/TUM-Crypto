public void run() {
    System.out.printf("New Client Connect! Connected IP : %s, Port : %d\n", connection.getInetAddress(), connection.getPort());

    try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String message = "hello!";
        String line = br.readLine();
        if(line== null)
            return;
        String[] splited = line.split(" ");
        if(splited[1].equals("/websocket") ) {
            String key="";
                ScreenCapture sc = new ScreenCapture();

                while(!"".equals(line)){
                    System.out.println("header : {}"+line);
                    if (line.contains("Sec-WebSocket-Key")){
                        key = line.split(": ")[1];
                    }
                    line= br.readLine();
                }

                    try {
                        byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                                + "Connection: Upgrade\r\n"
                                + "Upgrade: websocket\r\n"
                                + "Sec-WebSocket-Accept: "
                                + DatatypeConverter
                                .printBase64Binary(MessageDigest.getInstance("SHA-1").digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                        .getBytes("UTF-8")))
                                + "\r\n\r\n")
                                .getBytes("UTF-8");
                        DataOutputStream dos = new DataOutputStream(out);

                        dos.write(response, 0, response.length);

                        while(true) {
                            sc.captureStart();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ImageIO.write(sc.image, "png", byteArrayOutputStream);
                        byte[] buffer = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encode(buffer);
                        try{
                            dos.write(brodcast(encodedImage));
                        }catch (SocketException se){

                        }


                        }
                        //  dos.flush();

                    } catch (NoSuchAlgorithmException e) {

                    }
        }else{

            while(!"".equals(line)){
                System.out.println("header : {}"+line);
                line= br.readLine();
            }

                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./web/index.html").toPath());
                dos.write(body, 0, body.length);
                dos.writeBytes("\r\n");
                dos.flush();
            }
