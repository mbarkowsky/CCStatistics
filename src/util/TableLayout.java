package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class TableLayout implements LayoutManager {

	private int columns;
	
	public TableLayout(int columns) {
		this.columns = columns;
	}

	@Override
	public void addLayoutComponent(String name, Component component) {
		
	}

	@Override
	public void layoutContainer(Container target) {
		int columnWidth = target.getWidth() / columns;
		
		int column = 0;
		int row = 0;
		int rowHeight = 0;
		for(Component component:target.getComponents()){
			if(column == columns){
				column = 0;
				row += rowHeight;
				rowHeight = 0;
			}
			int cWidth = component.getPreferredSize().width;
			int cHeight = component.getPreferredSize().height;
			component.setBounds(column * columnWidth, row, cWidth, cHeight);
			if(cHeight > rowHeight){
				rowHeight = cHeight;
			}
			
			column++;
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return null;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return null;
	}

	@Override
	public void removeLayoutComponent(Component parent) {

	}

}
