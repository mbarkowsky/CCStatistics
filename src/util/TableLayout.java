package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class TableLayout implements LayoutManager {

	public static final int SCALING_COLUMN_WIDTH = -1;
	public static final int MINIMUM_COLUMN_WIDTH = -2;
	
	private static int[] getScalingWidths(int size) {
		int[] widths = new int[size];
		for(int i = 0; i < size; i++){
			widths[i] = SCALING_COLUMN_WIDTH;
		}
		return widths;
	}
	
	private int columns;
	private int[] columnTypes;
	
	public TableLayout(int columns) {
		this(columns, getScalingWidths(columns));
	}

	public TableLayout(int columns, int[] columnTypes) {
		if(columnTypes.length != columns){
			System.err.println("column number unclear");
			columnTypes = getScalingWidths(columns);
		}
		this.columns = columns;
		this.columnTypes = columnTypes;
	}

	@Override
	public void addLayoutComponent(String name, Component component) {
		
	}

	@Override
	public void layoutContainer(Container target) {
		int[] columnWidths = calculateWidths(target);
		int[] offsetsX = calculateOffsetsX(columnWidths);
		Component[] components = target.getComponents();
		
		int column = 0;
		int offsetY = 0;
		int rowHeight = 0;
		for(Component component:components){
			if(column == columns){
				column = 0;
				offsetY += rowHeight;
				rowHeight = 0;
			}
			
			int columnWidth = columnWidths[column];
			int cWidth = component.getPreferredSize().width;
			cWidth = cWidth < columnWidth ? cWidth : columnWidth;
			int cHeight = component.getPreferredSize().height;
			
			component.setBounds(offsetsX[column], offsetY, cWidth, cHeight);
			if(cHeight > rowHeight){
				rowHeight = cHeight;
			}
			
			column++;
		}
	}

	private int[] calculateWidths(Container target) {
		int[] widths = new int[columns];
		Component[] components = target.getComponents();
		
		for(int i = 0; i < components.length; i++){
			int column = i%columns;
			
			int columnType = columnTypes[column];
			if(columnType == MINIMUM_COLUMN_WIDTH){
				int cWidth = components[i].getPreferredSize().width;
				widths[column] = widths[column] < cWidth ? cWidth : widths[column];
			}
			else{
				widths[column] = columnType;
			}
		}
		
		int totalConstantWidth = 0;
		int scalingColumns = 0;
		for(int column = 0; column < widths.length; column++){
			int columnType = columnTypes[column];
			if(columnType != SCALING_COLUMN_WIDTH){
				totalConstantWidth += widths[column];
			}
			else{
				scalingColumns++;
			}
		}
		
		if(scalingColumns > 0){
			int scaledColumnWidth = (target.getWidth() - totalConstantWidth) / scalingColumns;
			for(int column = 0; column < widths.length; column++){
				int columnType = columnTypes[column];
				if(columnType == SCALING_COLUMN_WIDTH){
					widths[column] = scaledColumnWidth;
				}
			}
		}
		
		return widths;
	}
	
	private int[] calculateOffsetsX(int[] columnWidths) {
		int[] offsetsX = new int[columnWidths.length];
		for(int i = 0; i < offsetsX.length; i++){
			int offset = 0;
			for(int j = 0; j < i; j++){
				offset += columnWidths[j];
			}
			offsetsX[i] = offset;
		}
		return offsetsX;
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
