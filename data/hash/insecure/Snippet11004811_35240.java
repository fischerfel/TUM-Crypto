byte[] digest = null;
        MessageDigest md5;

        try {
            md5 = MessageDigest.getInstance("MD5");

            ZipEntry current;
            while((current = entry.getNextEntry()) != null){

                //ZipEntry current = entry.getNextEntry();
                System.out.println("Size:" + current.getSize());
                System.out.println("Name:" + current.getName());

                if(current.isDirectory()){
                    digest = this.encodeUTF8(current.getName());
                    md5.update(digest);
                }
                else{
                    int size = (int)current.getSize();
                    digest = new byte[size];
                    entry.read(digest, 0, size);
                    md5.update(digest);
                }
            }
            digest = md5.digest();
            entry.close();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
