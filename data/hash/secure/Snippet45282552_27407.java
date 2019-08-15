    MessageDigest instance = MessageDigest.getInstance("SHA-256");
    for(byte[] arr: source){
        instance.update(arr);
    }
    byte[] result = instance.digest();
