byte[] digest = new byte[this.BUFFER];
        MessageDigest md5;

        try {
            md5 = MessageDigest.getInstance("MD5");

            while(entry.getNextEntry() != null){

                ZipEntry current = entry.getNextEntry();

                if(current.isDirectory()){
                    digest = this.encodeUTF8(current.getName());
                    md5.update(digest);
                }
                else{
                        entry.read(digest, 0, this.BUFFER);
                        md5.update(digest);
                }
            }
            digest = md5.digest();
            entry.close();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
