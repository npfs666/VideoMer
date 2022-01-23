package test.jListView.swing;


import java.awt.Component;

public interface ListViewCellRenderer {
	
    Component getListViewCellRendererComponent(
            JListView listView,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus);

}
