package components.model;

import java.io.File;
import java.util.Arrays;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class ResultTreeTableModel extends AbstractTreeTableModel {

	public ResultTreeTableModel() {
		
		File root = new File("F:\\");
		
		for( String s : root.list() ) {
			System.out.println(s);
		}
		File child1 = new File(root, "D:\\");
		File child2 = new File(root, "C:\\");
		//super(new File("D:\\"));
        this.root = root;
        this.modelSupport = new TreeModelSupport(this);
	}
	
    @Override
    public Class<?> getColumnClass(int column) {
    	
        switch (column) {
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
        default:
            return super.getColumnClass(column);
        }
        
        //return getValueAt(0, column).getClass();
    }
    
	public int getRowCount() {
		return 10;
	}
    
    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "Nom";
        case 1:
            return "true";
        case 2:
            return "Type";
        case 3:
            return "Code";
        case 4:
        	return "Association";
        default:
            return super.getColumnName(column);
        }
    }

	@Override
	public Object getValueAt(Object node, int column) {
        if (node instanceof File) {
        	
            File file = (File) node;
            
            switch (column) {
            case 0:
                return file.getName();
            case 1:
                return "";
            case 2:
                return "";
            case 3:
                return "";
            case 4:
            	return "";
            }
        }

        return null;
	}

	@Override
	public Object getChild(Object parent, int index) {
        
        File parentFile = (File) parent;
        String[] children = parentFile.list();
        
        if (children != null) {
            return new File(parentFile, children[index]);
        }
        
        return null;
	}

	@Override
	public int getChildCount(Object parent) {
		
        if (parent instanceof File) {
            String[] children = ((File) parent).list();
            
            if (children != null) {
                return children.length;
            }
        }

        return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		
        if (parent instanceof File && child instanceof File) {
            File parentFile = (File) parent;
            File[] files = parentFile.listFiles();
            
            Arrays.sort(files);
            
            for (int i = 0, len = files.length; i < len; i++) {
                if (files[i].equals(child)) {
                    return i;
                }
            }
        }
        
        return -1;
	}
	
    /**
     * {@inheritDoc}
     */
    public boolean isCellEditable(Object node, int column) {
        // RG: Fix Issue 49 -- Cell not editable, by default.
        // Subclasses might override this to return true.
        return true;
    }

}
