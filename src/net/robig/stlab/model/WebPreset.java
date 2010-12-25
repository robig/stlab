package net.robig.stlab.model;

import net.robig.net.XmlParser.XmlElement;

public class WebPreset {
	private int id=0;
	String title="";
	String tags="";
	String description="";
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
			wp.setId(Integer.parseInt(presetElement.getAttribute("id")));
			wp.preset=p;
			XmlElement owner=presetElement.find("owner").get(0);
			wp.owner=WebUser.fromXml(owner);
		}catch(Exception ex){
			throw new InvalidXmlException(ex.getMessage());
		}
		return wp;
	}
	
	public String toString() {
		return "Preset #"+getId()+": Ó"+title+"Ó \n"+
			owner+"\n"+
			preset+"\n";
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StPreset getData() {
		return preset;
	}

	public void setData(StPreset preset) {
		this.preset = preset;
	}
}
