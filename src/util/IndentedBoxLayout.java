package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class IndentedBoxLayout implements LayoutManager {

	private int indent;
	
	public IndentedBoxLayout(int indent){
		this.indent = indent;
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Component[] components = parent.getComponents();

		int width = 0;
		int height = 0;
		for(Component component:components){
			Dimension size = component.getPreferredSize();
			if(size.width + indent > width){
				width = size.width + indent;
			}
			height += size.height;
		}
		
		return new Dimension(width, height);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Component[] components = parent.getComponents();

		int width = 0;
		int height = 0;
		for(Component component:components){
			Dimension size = component.getMinimumSize();
			if(size.width + indent > width){
				width = size.width + indent;
			}
			height += size.height;
		}
		
		return new Dimension(width, height);
	}

	@Override
	public void layoutContainer(Container parent) {
		Component[] components = parent.getComponents();
		
		int parentWidth = parent.getWidth();
		int offset = 0;
		for(Component component:components){
			Dimension size = component.getPreferredSize();
			component.setBounds(indent, offset, size.width < parentWidth ? size.width : parentWidth, size.height);
			offset += size.height;
		}
	}

}
