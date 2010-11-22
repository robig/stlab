package net.robig.gui;

import java.util.LinkedList;

public class TapButton extends ImageButton {
	private static final long serialVersionUID = 1L;
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
	public void onLeftClick() {
		removeOld();
		add();
	}
	
	public long getMean(int max){
		Long last=new Long(0);
		Long sum=new Long(0);
		int size=lastValues.size();
		int count=max>size?size:max;
		String vals="";
		int start=size>count?size-count-1:0;
		for(int i=start;i<size;i++){
			Long v=lastValues.get(i);
			if(last>0){
				long diff=last-v;
				//log.debug("v="+v+" last="+last+" last-v="+(last-v));
				sum+=last-v;
				vals+=" "+diff;
			}
			last=v;
		}
		log.debug("values: "+vals+" sum: "+sum+" count="+count);
		return sum/count;
	}
}
