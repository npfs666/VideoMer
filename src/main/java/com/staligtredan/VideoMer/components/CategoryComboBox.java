package com.staligtredan.VideoMer.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.staligtredan.VideoMer.components.model.ResultComboBoxModel;
import com.staligtredan.VideoMer.controleur.DefaultController;

/**
 * JComboBox générique multilingue avec retour en code & non affichage la
 * hashmap fourni l'emplacement de la traduction en clé & un code quelconque en
 * valeur (caché)
 * 
 * @author Brendan
 *
 * @param <E>
 */
public class CategoryComboBox<E> extends JComboBox<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2133712108110077635L;



	public CategoryComboBox() {
		super();
	}



	@SuppressWarnings("unchecked")
	public CategoryComboBox( DefaultController controller, HashMap<String, String> data ) {
		super();

		ResourceBundle bundle = controller.getBundle();
		ArrayList<ResultComboBoxModel> listeChoix = new ArrayList<ResultComboBoxModel>();

		// Getting Set of keys for Iteration
		Set<Entry<String, String>> stockSet = data.entrySet();

		// Using Iterator to loop Map in Java, here Map implementation is
		// Hashtable
		Iterator<Entry<String, String>> i = stockSet.iterator();

		// Iterator begins
		while ( i.hasNext() ) {

			Map.Entry<String, String> m = i.next();

			ResultComboBoxModel entry = new ResultComboBoxModel();
			entry.setLibelle(bundle.getString(m.getKey()));
			entry.setUrl(m.getValue());
			listeChoix.add(entry);
		}

		setModel(new DefaultComboBoxModel<E>((E[]) listeChoix.toArray()));
	}
}
