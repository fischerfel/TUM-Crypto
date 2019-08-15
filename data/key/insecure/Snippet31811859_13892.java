try {
            String secret = "secret";
            String message = "Message";

            Mac sha_HMAC = Mac.getInstance("HmacSHA512");

            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
            sha_HMAC.init(secret_key);

            String hash = Base64.encodeToString(sha_HMAC.doFinal(message.getBytes()), Base64.DEFAULT);
            System.out.println(hash);
            Log.e("string is ",hash);

        }
        catch (Exception e){
            System.out.println("Error");
        }
