        // hash pass one
        byte[] inDigest;
        MessageDigest digester= MessageDigest.getInstance("SHA-256"); // returns 256bits/ 32 bytes
        byte[] message= password.getBytes("UTF8");  
        digester.update(message); // append message
        inDigest= digester.digest(); // no salt
        byte[] outDigest= new byte[lengthKey];
        for (int i=0; i<lengthKey; i++){ // truncate bytes
            outDigest[i]= inDigest[i];
        }
        return outDigest;
