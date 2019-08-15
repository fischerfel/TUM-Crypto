byte[] my_key = (SALT2 + username + my_custom_secret_key).getBytes("UTF-8");
SecretKeySpec secretKeySpec = new SecretKeySpec(my_key, "AES");
