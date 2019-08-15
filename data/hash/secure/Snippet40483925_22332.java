mddigest   = java.security.MessageDigest.getInstance('MD5'); 

bufsize = 8192;

fid = fopen(filename);

while ~feof(fid)
    [currData,len] = fread(fid, bufsize, '*uint8');       
    if ~isempty(currData)
        mddigest.update(currData, 0, len);
    end
end

fclose(fid);

hash = reshape(dec2hex(typecast(mddigest.digest(),'uint8'))',1,[]);
