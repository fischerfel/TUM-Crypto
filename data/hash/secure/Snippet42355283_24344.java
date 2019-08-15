            objectFile = File(fullFilePath);
            fileInputStream = FileInputStream(objectFile);
            data = IOUtils.toString(fileInputStream, 'UTF-8');

            persistent digest;            

            if isempty(digest)
                digest = MessageDigest.getInstance('SHA-256');
            end

            hash = digest.digest(java.lang.String(data).getBytes('UTF-8'));
