System.security.Cryptography.RijndaelManaged AES = new System.Security.Cryptography.RijndaelManaged();
System.Security.Cryptography.MD5CryptoServiceProvider Hash_AES = new System.Security.Cryptography.MD5CryptoServiceProvider();

final MessageDigest Hash_AES = MessageDigest.getInstance("MD5");

        String encrypted = "";
        try {
            byte[] hash = new byte[32];
            byte[] temp = Hash_AES.ComputeHash(System.Text.ASCIIEncoding.ASCII.GetBytes(pass));
            final byte[] temp = Hash_AES.digest(pass.getBytes("US-ASCII"));

            Array.Copy(temp, 0, hash, 0, 16);
            Array.Copy(temp, 0, hash, 15, 16);
            AES.Key = hash;
            AES.Mode = System.Security.Cryptography.CipherMode.ECB;
            System.Security.Cryptography.ICryptoTransform DESEncrypter = AES.CreateEncryptor();
            byte[] Buffer = System.Text.ASCIIEncoding.ASCII.GetBytes(input);
            encrypted = Convert.ToBase64String(DESEncrypter.TransformFinalBlock(Buffer, 0, Buffer.Length));
        } catch (Exception ex) {

        }
        return encrypted;
