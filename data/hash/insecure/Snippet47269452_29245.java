            // you must first load your attachment to be able to call .getContent() on it
            fileAttachment.load();

            // load content
            byte[] b = fileAttachment.getContent();

            // get the hash of your attachment
            byte[] hash = MessageDigest.getInstance("MD5").digest(b);

            // after you run this on the attachment you do not want, take the string from 'actual' below and put it here
            String expected = "YOUR HASHED STRING";

            String actual = DatatypeConverter.printHexBinary(hash);
            System.out.println(actual);

            // some conditional to check if your current hash matches expected
            if(actual.equals(expected)){
                continue;
            }
