package com.staligtredan.VideoMer.components;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Document;
import javax.swing.text.Position;

/**
 * Surclasse de JTextField pour y ajouter une complétion automatique à partir
 * d'un ArrayList<String> Choisir les Séparateurs, taille de la liste etc...
 * 
 * @author Brendan
 * @version 0.1 13/11/2012
 */
public class JTextFieldAuto extends JTextField {

	private static final long serialVersionUID = 3749762405750942440L;

	/**
	 * Le popup menu qui va suggérer les réponses
	 */
	private JPopupMenu popSuggestions = new JPopupMenu();

	/**
	 * 
	 */
	private JScrollPane scrollSuggestions = new JScrollPane();

	/**
	 * La liste des suggestions
	 */
	private JList<String> suggestionsList = new JList<String>();



	public JTextFieldAuto() {

		super();
		createAutoCompletionPopupMenu();
	}



	public JTextFieldAuto( String text ) {

		super(text);
		createAutoCompletionPopupMenu();
	}



	public JTextFieldAuto( int columns ) {

		super(columns);
		createAutoCompletionPopupMenu();
	}



	public JTextFieldAuto( String text, int columns ) {

		super(text, columns);
		createAutoCompletionPopupMenu();
	}



	public JTextFieldAuto( Document doc, String text, int columns ) {

		super(doc, text, columns);
		createAutoCompletionPopupMenu();
	}



	/**
	 * Création du popupMenu qui va suggérer les réponses
	 */
	private void createAutoCompletionPopupMenu() {

		popSuggestions = new JPopupMenu();
		scrollSuggestions = new JScrollPane();
		suggestionsList = new JList<String>();

		// Pas de barre horizontale sur l'ascensseur
		scrollSuggestions.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// Nombre de lignes visibles
		suggestionsList.setVisibleRowCount(6);
		// Gestion de la selection
		suggestionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollSuggestions.setViewportView(suggestionsList);
		popSuggestions.add(scrollSuggestions);
	}



	/**
	 * Active les KeyListeners, et gère les suggestions à partir de la liste
	 * fournie
	 * 
	 * @param data
	 *            Liste des éléments à suggérer
	 */
	public void enableAutoCompletion( final ArrayList<String> data ) {

		// Double clic sur liste
		suggestionsList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked( MouseEvent e ) {

				if( e.getClickCount() > 1 ) {
					String[] tab = getText().split(",");

					if( tab.length >= 1 ) {
						String tmp = "";
						for ( int i = 0; i < tab.length - 1; i++ )
							tmp += tab[i].trim() + ", ";
						setText(tmp + suggestionsList.getSelectedValue());
					} else
						setText("" + suggestionsList.getSelectedValue());
					popSuggestions.setVisible(false);
				}
			}
		});

		// Touche 'Entrée' sur liste
		suggestionsList.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased( KeyEvent e ) {

				if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					if( popSuggestions.isVisible() ) {
						String[] tab = getText().split(",");

						if( tab.length >= 1 ) {
							String tmp = "";
							for ( int i = 0; i < tab.length - 1; i++ )
								tmp += tab[i].trim() + ", ";
							setText(tmp + suggestionsList.getSelectedValue());
						} else
							setText("" + suggestionsList.getSelectedValue());
						popSuggestions.setVisible(false);
					}
				}
			}
		});

		// Création du listener sur la frappe clavier (affichage popup,
		// déplacement list, validation, séparateur...)
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased( KeyEvent e ) {

				if( data.isEmpty() )
					; // Si la liste n'existe pas on sort
				else if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
					popSuggestions.setVisible(false);
				else if( e.getKeyCode() == KeyEvent.VK_UP ) {
					suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex() - 1);
					suggestionsList.ensureIndexIsVisible(suggestionsList.getSelectedIndex());
					requestFocus();
				} else if( e.getKeyCode() == KeyEvent.VK_DOWN ) {
					suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex() + 1);
					suggestionsList.ensureIndexIsVisible(suggestionsList.getSelectedIndex());
					requestFocus();
				} else if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					if( popSuggestions.isVisible() ) {
						String[] tab = getText().split(",");

						if( tab.length >= 1 ) {
							String tmp = "";
							for ( int i = 0; i < tab.length - 1; i++ )
								tmp += tab[i].trim() + ", ";
							setText(tmp + suggestionsList.getSelectedValue());
						} else
							setText("" + suggestionsList.getSelectedValue());
						popSuggestions.setVisible(false);
					}
				}

				// Affichage de la liste des acteurs sur un appui de ','
				else if( e.getKeyChar() == KeyEvent.VK_COMMA ) {
					Point p = getCaret().getMagicCaretPosition();

					setText(getText() + " ");
					// suggestionsList.setListData((String[])data.toArray());
					suggestionsList.setListData(data.toArray(new String[data.size()]));
					popSuggestions.pack();
					popSuggestions.show(JTextFieldAuto.this, (int) p.getX(), (int) p.getY() + getHeight());
					suggestionsList.setSelectedIndex(0);
					requestFocus();
				} else if( getText().trim().isEmpty() ) {
					popSuggestions.setVisible(false);
				}
				// Si la liste est affichée et qu'on entre un caractère
				// on va selectioner l'élément qui lui correspond le +
				else if( popSuggestions.isVisible() ) {
					String[] tab = getText().split(",");
					String dernierElement = tab[tab.length - 1].trim();
					Point p = getCaret().getMagicCaretPosition();
					popSuggestions.pack();
					popSuggestions.show(JTextFieldAuto.this, (int) p.getX(), (int) p.getY() + getHeight());

					if( dernierElement.trim().isEmpty() ) {
						suggestionsList.setSelectedIndex(0);
					} else {
						int lastIndex = suggestionsList.getSelectedIndex();
						int index = suggestionsList.getNextMatch(dernierElement, lastIndex, Position.Bias.Forward);
						if( index != -1 ) {
							suggestionsList.setSelectedIndex(index);
							suggestionsList.ensureIndexIsVisible(index);
						} else
							popSuggestions.setVisible(false);
					}
					requestFocus();
				}

			}



			@Override
			public void keyPressed( KeyEvent e ) {

				JTextField source = (JTextField) e.getSource();

				if( source.getText().trim().isEmpty() && !popSuggestions.isVisible()
					&& e.getKeyCode() != KeyEvent.VK_TAB && e.getKeyCode() != KeyEvent.VK_BACK_SPACE
					&& !data.isEmpty() ) {

					suggestionsList.setListData(data.toArray(new String[data.size()]));
					popSuggestions.pack();
					popSuggestions.show(source, 0, source.getHeight());
					suggestionsList.setSelectedIndex(0);
					source.requestFocus();
				}

			}
		});
	}



	/**
	 * Renvoie une ArrayList de String correspondant à un découpage par virgule
	 * 
	 * @return
	 */
	public ArrayList<String> toArray() {

		ArrayList<String> list = new ArrayList<String>();

		for ( String s : getText().split(",") ) {
			list.add(s.trim());
		}

		return list;
	}
}
