package com.staligtredan.VideoMer.util;

import java.awt.Container;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.BreakIterator;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * <code>HtmlParsing</code> contient toutes les m�thodes n�c�ssaires � l'analyse de sources HTML
 * 
 * @author Brendan
 * @since 0.1 1/10/2010
 * @version 1.0.0 21/01/2015 (rajout changeHTMLCodes)
 */
public class HtmlParsing {

	/**
	 * T�l�charge le contenu d'une page HTML � partir de son lien
	 * 
	 * @param link
	 *            URL
	 * 
	 * @return le contenu
	 */
	public static StringBuffer getSourceFrom( String link ) throws IOException {

		StringBuffer buff = new StringBuffer();

		try {
			// creation d'un objet URL
			URL url = new URL(link);
			// on etablie une connection a cette url
			URLConnection uc = url.openConnection();
			// on y cree un flux de lecture en UTF-8
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			// on lit le premier bit
			int c = br.read();
			// tant que c n'est pas egale au bit indiquant la fin d'un flux...
			while ( c != -1 ) {
				buff.append((char) c);
				// ...on l'ajoute dasn le StringBuilder...
				c = br.read();
				// ...on lit le suivant
			}

		}
		catch ( MalformedURLException e ) {
			e.printStackTrace();
		}

		return buff;
	}



	/**
	 * Renvoie une chaine nettoy�e de tout les caract�res d'espacement (Retour a la ligne, Tabulation, Retour chariot, Double
	 * espace, Tabulation)
	 * 
	 * @param src
	 *            La chaine � nettoyer
	 * @return La nouvelle chaine
	 */
	public static String removeBadChar( String src ) {

		return src.replaceAll("\n", "") // Retour a la ligne
				.replaceAll("\t", "") // Tabulation
				.replaceAll("\r", "") // Retour chariot
				.replaceAll("\\s{2,}","") // space >= 2 supprim�s
				.replaceAll("&nbsp;", ""); // Tabulation
	}
	
	
	/**
	 * Change le code &#039; en apostrophe, possibilit� de rajouter d'autres trucs a probl�mes par la suite
	 * 
	 * @param src
	 * @return
	 */
	public static String changeHTMLCodes( String src ) {
		
		return src.replaceAll("&#039;", "'");
	}


	/**
	 * Découpe un texte avec des sauts de lignes pour correspondre à la taille du JLabel
	 * 
	 * 
	 * @param label
	 *            le JLabel de référence
	 * @param text
	 *            Le texte à découper
	 * 
	 * @return Le texte encodé en html & découpé
	 */
	public static String wrapLabelText( JLabel label, String text ) {

		FontMetrics fm = label.getFontMetrics(label.getFont());
		Container container = label.getParent();
		
		int containerWidth = (int) (container.getWidth() * 0.95);

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);

		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer("<html>");

		int start = boundary.first();
		for ( int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next() ) {
			String word = text.substring(start, end);
			trial.append(word);
			int trialWidth = SwingUtilities.computeStringWidth(fm, trial.toString());
			if ( trialWidth > containerWidth ) {
				trial = new StringBuffer(word);
				real.append("<br>");
			}
			real.append(word);
		}

		real.append("</html>");

		return real.toString();
	}



	/**
	 * T�l�charge le contenu d'une page HTML � partir de son lien
	 * 
	 * @param link
	 *            URL
	 * @param referer
	 * @return le contenu
	 */
	public static StringBuffer getSourceFrom( String link, String referer ) throws UnknownHostException {

		StringBuffer buff = new StringBuffer();

		try {
			// creation d'un objet URL
			URL url = new URL(link);
			// on etablie une connection a cette url
			URLConnection uc = url.openConnection();
			// On ajoute le referer
			uc.addRequestProperty("Referer", referer);
			// on y cree un flux de lecture en UTF-8
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			// on lit le premier bit
			int c = br.read();
			// tant que c n'est pas egale au bit indiquant la fin d'un flux...
			while ( c != -1 ) {
				buff.append((char) c);
				// ...on l'ajoute dasn le StringBuilder...
				c = br.read();
				// ...on lit le suivant
			}

		}
		catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		return buff;
	}



	/**
	 * Teste si une chaine est compos�e uniquement de caract�res num�rique
	 * 
	 * @param str
	 *            Chaine de caract�res
	 * @return Si str = "", retour vrai
	 */
	public static boolean isNumeric( String str ) {

		return Pattern.matches("[0-9]*", str);
	}



	/**
	 * Keep Src between Start and End
	 * 
	 * @param start
	 * @param end
	 * @param src
	 * @param afterStart
	 * @param afterEnd
	 * @return
	 */
	public static boolean keepBetween( String start, String end, StringBuffer src, boolean afterStart, boolean afterEnd ) {

		final int IoS = src.indexOf(start);

		if ( IoS != -1 ) {

			final int IoE = src.indexOf(end, IoS);

			if ( IoE != -1 ) {

				src.delete(IoE + (afterEnd ? end.length() : 0), src.length());
				src.delete(0, IoS + (afterStart ? start.length() : 0));

				return true;
			}
			else
				return false;
		}
		else
			return false;
	}



	/**
	 * Return the string between specified strings <br>
	 * The source is modified if RmLeftOfSource = true <br>
	 * AfterStart = true get from the last character of the string of start <br>
	 * After = true get until the last character of the string of end
	 * 
	 * @param start
	 * @param end
	 * @param src
	 * @param afterStart
	 * @param afterEnd
	 * @param rmLeftOfSrc
	 * @return
	 */
	public static String getBetween( String start, String end, StringBuffer src, boolean afterStart, boolean afterEnd,
			boolean rmLeftOfSrc ) {

		String toReturn = "";
		final int IoS = src.indexOf(start);

		if ( IoS != -1 ) {
			final int IoE = src.indexOf(end, IoS);

			if ( IoE != -1 ) {
				toReturn = src.substring(IoS + (afterStart ? start.length() : 0), IoE + (afterEnd ? end.length() : 0));
				toReturn = toReturn.trim();
				if ( rmLeftOfSrc )
					src.delete(0, IoE + end.length());
			}
		}
		return toReturn;
	}



	/**
	 * Met en forme les chaines virgul�es
	 * 
	 * @param str
	 *            Chaine � mettre en forme
	 * @return La nouvelle chaine
	 */
	public static String cleanList( String str ) {

		return str.replaceAll(",  ", ", ").replaceAll(" ,", ",");
	}



	/**
	 * Return the string from specified string <br>
	 * The source is modified if RmLeftOfSource = true <br>
	 * AfterStart = true get from the last character of the string of start
	 * 
	 * @param start
	 * @param src
	 * @param afterStart
	 * @return
	 */
	public static String getFrom( String start, String src, boolean afterStart ) {

		final int IoS = src.indexOf(start);
		if ( IoS != -1 ) {
			src = src.substring(IoS + (afterStart ? start.length() : 0));
			src = src.trim();
		}
		else
			src = "";
		return src;
	}



	/**
	 * Return the string between specified string <br/>
	 * The source is not modified<br/>
	 * AfterStart=true get from the last character of the string of start<br/>
	 * AfterEnd=true get until the last character of the string of end<br/>
	 * 
	 * @param start
	 * @param end
	 * @param src
	 * @param afterStart
	 * @param afterEnd
	 * @return
	 */
	public static String getBetween( String start, String end, String src, boolean afterStart, boolean afterEnd ) {

		final int IoS = src.indexOf(start);

		if ( IoS != -1 ) {
			final int IoE = src.indexOf(end, IoS);
			if ( IoE != -1 ) {
				src = src.substring(IoS + (afterStart ? start.length() : 0), IoE + (afterEnd ? end.length() : 0));
				src = src.trim();
			}
			else
				src = "";
		}
		else
			src = "";
		return src;
	}



	/**
	 * Remove what is inside characters specified
	 * 
	 * @param src
	 * @param start
	 * @param end
	 * @return
	 */
	public static String removeInside( String src, String start, String end ) {

		int IoS, IoE;
		// We remove what is inside <>
		IoS = src.indexOf(start);
		IoE = src.indexOf(end, IoS);
		while ( IoS != -1 && IoE != -1 ) {
			src = src.substring(0, IoS) + src.substring(IoE + 1);
			IoS = src.indexOf(start);
			IoE = src.indexOf(end, IoS);
		}
		src = src.trim();
		return src;
	}



	/**
	 * Enleve le code qui se trouve entre deux balises <...> < / ...>
	 * 
	 * @param Src
	 * 
	 * @return
	 */
	public static String removeMarkup( String src ) {

		return removeInside(src, "<", ">");
	}



	/**
	 * Renvoie une chaine de caract�res encod�e au format URL
	 * 
	 * @param str
	 *            Chaine � encoder
	 * 
	 * @return La nouvelle chaine
	 */
	public static String encodeToUrl( String str ) {

		String line = "", hex;

		int size = str.length();
		// For each character
		for ( int i = 0; i < size; i++ ) {
			char c = str.charAt(i);
			// if it's a special character
			if ( c < ' ' || c > '~' ) {
				// We replace the character to the Unicode
				hex = Integer.toHexString(c);
				line += "%" + hex;
				// if it's a normal character
			}
			else {
				line += c;
			}
		}

		return line.replace(" ", "%20");
	}
}
