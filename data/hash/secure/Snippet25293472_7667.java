MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(newPassword.getBytes("UTF-16LE"));

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            //String newpass = new String(pwdArray, "UTF8");
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", sb.toString()));

            // Perform the update
            ctx.modifyAttributes(userName, mods);
