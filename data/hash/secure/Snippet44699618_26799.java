function str = hash(in)

% Get a bytestream from the input. Note that this calls saveobj.
inbs = getByteStreamFromArray(in);

% Create hash using Java Security Message Digest.
md = java.security.MessageDigest.getInstance('SHA1');
md.update(inbs);

% Convert to uint8.
d = typecast(md.digest, 'uint8');

% Convert to a hex string.
str = dec2hex(d)';
str = lower(str(:)');
