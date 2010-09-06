package net.robig.stlab.util.config;

import net.robig.stlab.util.config.types.Rectangle;

public class PositionSizeValue extends AbstractValue<Rectangle>{

	Rectangle value=new Rectangle();
	
	@Override
	public Rectangle getValue() {
		return value;
	}

	@Override
	public void setValue(Rectangle v) {
		value=v;
		//ObjectConfig.set
	}

	@Override
	public String toString() {
		return "Rectangle:"+value.x+","+value.y+","+value.width+","+value.heigth;
	}

	@Override
	public void fromString() {
		
	}
	
	
	
}
