package net.robig.stlab.model;

import net.robig.net.XmlParser.XmlElement;

public class WebPreset {
	int id=0;
	String title="";
	StPreset preset=null;
	WebUser owner=null;
	
	public static WebPreset fromXml(XmlElement presetElement) throws InvalidXmlException{
		WebPreset wp=new WebPreset();
		try {
			String title=presetElement.getAttribute("title");
			String data=presetElement.find("data").get(0).getText();
			StPreset p=new StPreset();
			p.parseParameters(data);
			p.setName(title);
			wp.title=title;
			wp.id=Integer.parseInt(presetElement.getAttribute("id"));
			wp.preset=p;
			XmlElement owner=presetElement.find("owner").get(0);
			wp.owner=WebUser.fromXml(owner);
		}catch(Exception ex){
			throw new InvalidXmlException(ex.getMessage());
		}
		return wp;
	}
	
	public String toString() {
		return "Preset #"+id+": Ó"+title+"Ó \n"+
			owner+"\n"+
			preset+"\n";
	}
}
