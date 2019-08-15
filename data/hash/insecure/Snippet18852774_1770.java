public DocumentByteArrayWithChecksum getRawBytesFromFile(String filename)
            throws IOException, Exception {

        DocumentByteArrayWithChecksum dba = new DocumentByteArrayWithChecksum();
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MessageDigest md = MessageDigest.getInstance("MD5");

        String SFTPHost = "my_IP_Address";
        int    SFTPPort = 22; // This is the default sftp port
        String SFTPUsername = "my_Username";
        String SFTPPassword = "my_Password";

        Channel channel = null;
        ChannelSftp channelsftp = null;
        JSch jsch = new JSch();
        com.jcraft.jsch.Session session = jsch.getSession(SFTPUsername,
                                                              SFTPHost,
                                                              SFTPPort);

        try {
            session.setPassword(SFTPPassword);
            session.connect();
            channelsftp = (ChannelSftp)session.openChannel("sftp");
            channelsftp.connect();

            in = channelsftp.get(filename);

            // used to get an MD5 length
            in = new DigestInputStream(in, md);
            logger.debug("The file is " + filename);
            long length = filename.length();
            if (length > Integer.MAX_VALUE) {
                logger.error("File is too large " + length);
                throw new Exception("file exceeds maximum length of "
                        + String.valueOf(Integer.MAX_VALUE));
            }

            byte[] buffer = new byte[(int) length];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            } // End of while block

        } catch(JSchException e) {
            System.err
            .println("%%%% Error in JSch process %%%%");
            e.printStackTrace();
        }
        catch(Exception e) {
            System.err
            .println("%%%% Error during getRawBytesFromFile function %%%%");
            e.printStackTrace();
        }
        finally {
            in.close();
            channelsftp.disconnect();
            session.disconnect();
        }// End of try block
        dba.orginalPDFDocumentChecksum = md.digest();
        dba.bytePDFDocument = out.toByteArray();

        return dba;
    }
