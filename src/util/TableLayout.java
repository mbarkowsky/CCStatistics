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
			cWidth = cWidth < columnWidth ? cWidth : columnWidth;
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
		int minWidth = 0;
		int minHeight = 0;
		Component[] components = parent.getComponents();
		for(int i = 0; i < components.length; i++){
			Dimension leftComponentSize = components[i].getMinimumSize();
			Dimension rightComponentSize = components[++i].getMinimumSize();
			
			int rowWidth = leftComponentSize.width + rightComponentSize.width;
			minWidth = Math.max(minWidth, rowWidth);
			
			int rowHeight = Math.max(leftComponentSize.height, rightComponentSize.height);
			minHeight += rowHeight;
		}
		return new Dimension(minWidth, minHeight);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int prefWidth = 0;
		int prefHeight = 0;
		Component[] components = parent.getComponents();
		for(int i = 0; i < components.length; i++){
			Dimension leftComponentSize = components[i].getPreferredSize();
			Dimension rightComponentSize = components[++i].getPreferredSize();
			
			int rowWidth = leftComponentSize.width + rightComponentSize.width;
			prefWidth = Math.max(prefWidth, rowWidth);
			
			int rowHeight = Math.max(leftComponentSize.height, rightComponentSize.height);
			prefHeight += rowHeight;
		}
		return new Dimension(prefWidth, prefHeight);
	}

	@Override
	public void removeLayoutComponent(Component parent) {

	}

}
