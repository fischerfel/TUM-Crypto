@Override
    public boolean sendData(String filename, SealedObject sealed, int len) throws RemoteException {
        try{
//            System.out.println("Here" );
//            System.setProperty("java.security.policy", "file:./<policy>.policy");
//            if (System.getSecurityManager() == null) {
//                System.setSecurityManager(new SecurityManager());
//            }

            key = KeyGenerator.getInstance("DES").generateKey();
            dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, key);
            File f=new File(filename);
            f.createNewFile();
            FileOutputStream out=new FileOutputStream(f,true);
            //byte[] data = (byte[]) sealed.getObject(dcipher);
            System.out.println("Original Object: " + sealed );

            //out.write(data,0,len);
            out.flush();
            out.close();
            System.out.println("Done writing data...");
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
