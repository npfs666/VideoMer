package com.staligtredan.VideoMer.components.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.staligtredan.VideoMer.components.ResultMutableTreeNode;
import com.staligtredan.VideoMer.components.VideoFileFilter;
import com.staligtredan.VideoMer.handler.ResultTreeModelEvent;
import com.staligtredan.VideoMer.handler.ResultTreeModelListener;

/**
 * Modèle de données pour le TreeTable de l'écran d'analyse
 * 
 * @author Brendan Jaouen
 * @version 0.1 6/07/2010
 */
public class ResultTreeTableModel extends AbstractTreeTableModel {

	/**
	 * Liste des dossiers à analyser
	 */
	private ArrayList<File> dossiers;

	/**
	 * Nombre de lignes actives
	 */
	private int activeRowCount;

	/**
	 * Liste des écouteurs sur l'object
	 */
	protected EventListenerList listeners;

	/**
	 * Extentions autorisées
	 */
	private String[] fileExtensionsAllowed;



	/**
	 * Construits un <code>ResultTreeTableModel</code> vide
	 */
	public ResultTreeTableModel( String[] fileExtensionsAllowed ) {

		super();
		dossiers = new ArrayList<File>();
		this.modelSupport = new TreeModelSupport(this);
		listeners = new EventListenerList();
		this.fileExtensionsAllowed = fileExtensionsAllowed;
	}



	/**
	 * Ajoute un fichier au tableau
	 * 
	 * @param file
	 */
	public void addFile( File file ) {

		dossiers.add(file);
	}



	/**
	 * Ajoute des fichiers au tableau
	 * 
	 * @param files
	 */
	public void addFiles( List<File> files ) {

		dossiers.addAll(files);
	}



	/**
	 * Met à jour l'arborescence
	 */
	public void updateTree() {

		createTree();
		modelSupport.firePathChanged(new TreePath(root));
	}



	/**
	 * Enlève un fichier du tableau
	 * 
	 * @param file
	 * @return
	 */
	public boolean delFile( File file ) {

		boolean b = dossiers.remove(file);
		updateTree();

		return b;
	}



	/**
	 * Crée le <code>TreeTable</code> à partir de la liste des <code>File</code>
	 * ajoutés par l'utilisateur
	 */
	private void createTree() {

		root = new ResultMutableTreeNode("Root");

		for ( File f : dossiers ) {

			if( f.isDirectory() ) {

				ResultMutableTreeNode tmp = new ResultMutableTreeNode(f.getPath());
				((ResultMutableTreeNode) root).add(tmp);
				createTree(f, tmp);

			} else {
				if( new VideoFileFilter(fileExtensionsAllowed).accept(f.getParentFile(), f.getName()) ) {
					ResultMutableTreeNode node = new ResultMutableTreeNode(f.getName());
					node.setFilePath(f.getPath());
					((ResultMutableTreeNode) root).add(node);
				}
			}
		}
	}



	/**
	 * Méthode récursive qui permet de créer chaque noeud du
	 * <code>TreeTable</code>
	 * 
	 * @param fileParent
	 *            le fichier parent
	 * @param nodeParent
	 *            le noeud parent
	 */
	private void createTree( File fileParent, ResultMutableTreeNode nodeParent ) {

		VideoFileFilter vff = new VideoFileFilter(fileExtensionsAllowed);

		for ( File f : fileParent.listFiles(vff) ) {

			if( f.isDirectory() ) {
				ResultMutableTreeNode node = new ResultMutableTreeNode(f.getName());
				nodeParent.add(node);
				createTree(f, node);

			} else {
				ResultMutableTreeNode node = new ResultMutableTreeNode(f.getName());
				node.setFilePath(f.getPath());
				nodeParent.add(node);
			}
		}

	}



	/**
	 * Renvoie le nombre de lignes
	 * 
	 * @return
	 */
	public int getRowCount() {

		return ((ResultMutableTreeNode) root).getLeafCount();
	}



	/**
	 * Renvoie le nombre de lignes actives
	 * 
	 * @return
	 */
	public int getActiveRowCount() {

		activeRowCount = 0;

		listChildren((ResultMutableTreeNode) root);

		return activeRowCount;
	}



	/**
	 * Liste tous les éléments actif d'un <code>ResultMutableTreeNode</code>
	 * Méthode récursive
	 * 
	 * @param node
	 *            Le noeud � partir duquel on compte les �l�ments et ses enfants
	 */
	private void listChildren( ResultMutableTreeNode node ) {

		for ( Enumeration<?> e = node.children(); e.hasMoreElements(); ) {

			ResultMutableTreeNode elt = (ResultMutableTreeNode) e.nextElement();

			if( !elt.isLeaf() )
				listChildren(elt);
			else if( elt.isActif() ) {
				activeRowCount++;
			}
		}
	}



	/**
	 * Met tous les éléments actifs
	 * 
	 * @param value
	 */
	public void setAllActif( boolean value ) {

		ResultMutableTreeNode root = (ResultMutableTreeNode) this.root;
		changeChildrenActif(root, value);
	}



	@Override
	public Class<?> getColumnClass( int column ) {

		switch ( column ) {

		case 0:
			return String.class;
		case 1:
			return Boolean.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		default:
			return super.getColumnClass(column);
		}
	}



	@Override
	public void setValueAt( Object value, Object node, int column ) {

		if( node instanceof ResultMutableTreeNode ) {

			ResultMutableTreeNode file = (ResultMutableTreeNode) node;

			switch ( column ) {

			case 0:

				if( file.isLeaf() ) {
					file.setUserObject((String) value);
					firePathChanged(file);
				}
				break;

			case 1:

				file.setActif((Boolean) value);
				if( !file.isLeaf() ) {

					changeChildrenActif(file, (Boolean) value);
					modelSupport.firePathChanged(new TreePath(node));
				}
				break;

			case 2:

				file.setType((String) value);

				if( !file.isLeaf() ) {

					changeChildrenType(file, (String) value);
					modelSupport.firePathChanged(new TreePath(node));
				}
				break;

			case 3:

				file.setCode((String) value);
				modelSupport.firePathChanged(new TreePath(node));
				break;

			case 4:

				if( file.isLeaf() ) {

					file.setChoix((ResultComboBoxModel) value);
					modelSupport.firePathChanged(new TreePath(node));
				}

			}
		}
	}



	/**
	 * Gets the path from the root to the specified node.
	 * 
	 * @param aNode
	 *            the node to query
	 * @return an array of {@code TreeTableNode}s, where
	 *         {@code arr[0].equals(getRoot())} and
	 *         {@code arr[arr.length - 1].equals(aNode)}, or an empty array if
	 *         the node is not found.
	 * @throws NullPointerException
	 *             if {@code aNode} is {@code null}
	 */
	public TreeTableNode[] getPathToRoot( TreeTableNode aNode ) {

		List<TreeTableNode> path = new ArrayList<TreeTableNode>();
		TreeTableNode node = aNode;

		while ( node != root ) {
			path.add(0, node);

			node = node.getParent();
		}

		if( node == root ) {
			path.add(0, node);
		}

		return path.toArray(new TreeTableNode[0]);
	}



	/**
	 * Fonction récursive qui permet de changer tout les types (film,série) d'un
	 * noeuds et de ses enfants
	 * 
	 * @param node
	 *            le noeud de départ
	 * @param type
	 *            le type (film, série) à appliquer
	 */
	private void changeChildrenType( ResultMutableTreeNode node, String type ) {

		for ( Enumeration<?> e = node.children(); e.hasMoreElements(); ) {

			ResultMutableTreeNode elt = (ResultMutableTreeNode) e.nextElement();

			elt.setType(type);
			if( !elt.isLeaf() )
				changeChildrenType(elt, type);
		}
	}



	/**
	 * Fonction récursive qui permet de cocher ou décocher les éléments d'un
	 * noeuds et de ses enfants
	 * 
	 * @param node
	 *            le noeud de départ
	 * @param type
	 *            l'état à appliquer (vrai, faux)
	 */
	private void changeChildrenActif( ResultMutableTreeNode node, boolean actif ) {

		for ( Enumeration<?> e = node.children(); e.hasMoreElements(); ) {

			ResultMutableTreeNode elt = (ResultMutableTreeNode) e.nextElement();

			elt.setActif(actif);
			if( !elt.isLeaf() )
				changeChildrenActif(elt, actif);
		}
	}



	@Override
	public int getColumnCount() {

		return 5;
	}



	@Override
	public String getColumnName( int column ) {

		switch ( column ) {

		case 0:
			return "Nom";
		case 1:
			return "false";
		case 2:
			return "Type";
		case 3:
			return "Code";
		case 4:
			return "Association";
		case 5:
			return "Path";
		default:
			return super.getColumnName(column);
		}
	}



	@Override
	public Object getValueAt( Object node, int column ) {

		if( node instanceof ResultMutableTreeNode ) {

			ResultMutableTreeNode file = (ResultMutableTreeNode) node;

			switch ( column ) {

			case 0:
				return file.getUserObject();
			case 1:
				return file.isActif();
			case 2:
				return file.getType();
			case 3:
				return file.getCode();
			case 4:
				return file.getChoix().toString();
			case 5:
				return file.getFilePath();
			}
		}

		return null;
	}



	@Override
	public Object getChild( Object parent, int index ) {

		ResultMutableTreeNode parentFile = (ResultMutableTreeNode) parent;
		TreeNode child = parentFile.getChildAt(index);

		if( child != null )
			return child;

		return null;
	}



	@Override
	public int getChildCount( Object parent ) {

		if( parent instanceof ResultMutableTreeNode ) {

			ResultMutableTreeNode parentFile = (ResultMutableTreeNode) parent;

			return parentFile.getChildCount();
		}

		return 0;
	}



	@Override
	public int getIndexOfChild( Object parent, Object child ) {

		if( parent instanceof ResultMutableTreeNode && child instanceof ResultMutableTreeNode ) {

			ResultMutableTreeNode parentFile = (ResultMutableTreeNode) parent;
			ResultMutableTreeNode childFile = (ResultMutableTreeNode) child;

			return parentFile.getIndex(childFile);
		}

		return -1;
	}



	@Override
	public boolean isCellEditable( Object node, int column ) {

		if( node instanceof ResultMutableTreeNode ) {

			ResultMutableTreeNode file = (ResultMutableTreeNode) node;

			switch ( column ) {

			case 0:
				if( file.isLeaf() )
					return true;
				return false;
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 4:
				if( file.isLeaf() && !file.getChoix().isEmpty() )
					return true;
				return false;
			case 5:
				return false;
			default:
				return false;
			}
		}

		return false;
	}



	/**
	 * Adds a listener for the TreeModelEvent posted after the tree changes.
	 * 
	 * @param l
	 *            the listener to add
	 */
	public void addResultTreeModelListener( ResultTreeModelListener l ) {

		listeners.add(ResultTreeModelListener.class, l);
	}



	private void firePathChanged( ResultMutableTreeNode node ) {

		ResultTreeModelListener[] listeners = (ResultTreeModelListener[]) this.listeners
				.getListeners(ResultTreeModelListener.class);
		for ( int i = listeners.length - 1; i >= 0; i-- )
			listeners[i].treeNodesChanged(new ResultTreeModelEvent(this, node));
	}
}
