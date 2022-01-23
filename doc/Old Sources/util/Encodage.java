package com.staligtredan.VideoMer.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

/**
 * <code>Encodage</code> est une classe qui permet de d'encoder des objets
 * 
 * @author Brendan Jaouen
 * @version 0.1 1/10/2010
 */
public abstract class Encodage {

	/**
	 * Encode une chaine de caractère dans une suite numérique pour éviter le
	 * bordel des caractère spéciaux.
	 * 
	 * @param string
	 *            La chaine à encoder
	 * @return Le nombre en format <code>Long</code>
	 */
	public static long encodeChaine( String string ) {

		try {
			byte buffer[] = string.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			CheckedInputStream cis = new CheckedInputStream(bais, new Adler32());
			byte readBuffer[] = new byte[254];
			cis.read(readBuffer);
			return cis.getChecksum().getValue();
		} catch ( IOException e ) {
			e.printStackTrace();
			return 0;
		}
	}



	/**
	 * Encode une chaine en MD5
	 * 
	 * @param s
	 *            the String to encode
	 * 
	 * @return the String encoded or null
	 */
	public static String encodeNomFilm( String s ) {

		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("MD5").digest(s.getBytes());

			StringBuilder hashString = new StringBuilder();

			for ( int i = 0; i < hash.length; i++ ) {

				String hex = Integer.toHexString(hash[i]);

				if( hex.length() == 1 ) {
					hashString.append('0');
					hashString.append(hex.charAt(hex.length() - 1));
				} else
					hashString.append(hex.substring(hex.length() - 2));
			}

			return new String(hashString);

		} catch ( NoSuchAlgorithmException e ) {
			e.printStackTrace();
		}

		return null;
	}
}
