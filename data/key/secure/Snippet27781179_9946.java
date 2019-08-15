String username = recMessage.substring(0, recMessage.indexOf(' '));
                String ciphertxtWithNoName = recMessage.substring(recMessage.indexOf(':') + 2);

                scrtkey = new SecretKeySpec(secrtKeyByte, "AES");

                String plaintext = msgAESEnDe.decryptedPlain(ciphertxtWithNoName, scrtkey);
                System.out.println(username +" " + plaintext +"  <-- after decryption");

                //current time
                String time = log.format(new Date());

                // Displaying received message.
                ClientGUI.TA_CONVERSATION.append(time+ " ["+username.substring(0, username.length()-1) + "]: " + plaintext + "\n");
