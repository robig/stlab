package net.robig.gui;

import java.util.LinkedList;


public class TapButton extends ImageButton {

	LinkedList<Long> lastValues=new LinkedList<Long>();
	long old=5000;
	
	public TapButton() {
		imageFile="img/button.png";
		init();
	}
	
	private void removeOld(){
		long now=System.currentTimeMillis();
		
		while(lastValues.size()>0 && (now-lastValues.peek())>old){
			lastValues.pop();
		}
	}
	
	private void add(){
		long now=System.currentTimeMillis();
		lastValues.push(new Long(now));
	}
	
	@Override
	public void onClick() {
		removeOld();
		add();
	}
	
	public long getMean(int max){
		Long last=new Long(0);
		Long sum=new Long(0);
		int size=lastValues.size();
		int count=max;
		if(max>size) count=size;
		for(int i=0;i<size;i++){
			Long v=lastValues.get(i);
			if(last>0){
				//log.debug("v="+v+" last="+last+" last-v="+(last-v));
				sum+=last-v;
			}
			last=v;
		}
		//log.debug("sum: "+sum+" count="+count);
		return sum/count;
	}
}
