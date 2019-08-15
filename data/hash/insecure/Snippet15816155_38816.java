String value2=loginPasswordField.getText(); //The Password Field (I know getText() isnt secure)
MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(value2.getBytes());
                String value3 = new BigInteger(1, md.digest()).toString(16);
                System.out.println("It should work: "+value3);
