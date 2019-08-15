protected void onPostExecute(String result) {
            if (result == null) {
                tv.setText("NULL");
                return;
            }
            JsonElement jelement = new JsonParser().parse(result);
            JsonObject jobject = jelement.getAsJsonObject();
            String signature = jobject.getAsJsonPrimitive("signature").getAsString();
            BigInteger N = jobject.getAsJsonPrimitive("N").getAsBigInteger();
            BigInteger E = jobject.getAsJsonPrimitive("E").getAsBigInteger();
            String hash = jobject.getAsJsonPrimitive("hash").getAsString();
            java.security.spec.RSAPublicKeySpec spec = new java.security.spec.RSAPublicKeySpec(N, E);

            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey pk = keyFactory.generatePublic(spec);

                MessageDigest digest = MessageDigest.getInstance("SHA1");
                byte[] inputBytes = msg.getBytes("UTF8");
                byte[] hashedBytes = digest.digest(inputBytes);

                Signature sig = Signature.getInstance("SHA1withRSA", "SC");
                sig.initVerify( pk );
                sig.update( hashedBytes );
                boolean ret = sig.verify( Hex.decode(signature) );
                if (ret) {
                    tv.setText(output + "Verified");
                } else {
                    tv.setText(output + "NOT VERIFIED");
                }
            }
            catch (Exception e) {
                Log.i("error", e.toString());
            }  
        }
    }
