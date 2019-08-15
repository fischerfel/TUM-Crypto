String columns = "UserHash,EMail,Name,Gender,BirthYear,Birthday,MaritalStatus,UserID,ReferralUser,Likes";

String sql = "INSERT INTO Users ("+ columns+") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        byte[] bytesOfUserHash = user.getId().getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] userHash = md.digest(bytesOfUserHash);

        stmt  = con.prepareStatement(sql);
        stmt.setBytes(1,userHash);
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getName());
        stmt.setInt(4, user.getGender().value());
        stmt.setString(5, birthday.split("-")[0]);
        stmt.setString(6, birthday);
        stmt.setInt(7, user.getRelationshipStatus().value());
        stmt.setString(8, user.getId());
        stmt.setString(9, referraluser);
        stmt.setString(10, likesjson);
        stmt.executeUpdate();
