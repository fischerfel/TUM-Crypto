         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");

         cipher.init(
                 Cipher.DECRYPT_MODE,
                 new SecretKeySpec(keyMaterial, "AES"), //C# version??
                 new IvParameterSpec(iv)
         );
