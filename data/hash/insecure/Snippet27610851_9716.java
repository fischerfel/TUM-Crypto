   try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        for(Entry<String, Object> entry : allParams.entrySet()) {
            md.update(entry.getKey().getBytes());
            if(entry.getValue() instanceof String) {
                String value = (String) entry.getValue();
                md.update(value.getBytes());
            } else (...) // some other instance checking for non String values
        }
        String checksum = bytesToHex(md.digest());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
