package com.staligtredan.VideoMer.util;

import java.awt.Container;
import java.awt.FontMetrics;
import java.text.BreakIterator;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * <code>HtmlParsing</code> contient toutes les méthodes nécéssaires à l'analyse
 * de sources HTML
 * 
 * @author Brendan
 * @since 0.1 1/10/2010
 * @version 1.0.0 21/01/2015 (rajout changeHTMLCodes)
 */
public class HtmlParsing {

	/**
	 * Découpe un texte avec des sauts de lignes pour correspondre à la taille
	 * du JLabel
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
			if( trialWidth > containerWidth ) {
				trial = new StringBuffer(word);
				real.append("<br>");
			}
			real.append(word);
		}

		real.append("</html>");

		return real.toString();
	}
}
