try
        {
            String password = txtPassword.getPassword().toString();

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("Hex format : " + sb.toString());


        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
