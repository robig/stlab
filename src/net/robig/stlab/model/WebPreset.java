package net.robig.stlab.model;

import net.robig.net.XmlParser.XmlElement;

public class WebPreset {
	private int id=0;
	String title="";
	String tags="";
	String description="";
	StPreset preset=null;
	WebUser owner=null;
	int rating=0;
	
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public static String bool2Str(boolean b){
		return b?"on&nbsp;":"off";
	}
	
	public String toHtml(){
		return "<html><div><b>Description:</b></br>"+
			getDescription()+
			"</div><br/>"+
			"Amp: "+getData().getAmpName()+" ("+getData().getAmpTypeName()+")<br/>"+
			"volume="+getData().getVolume()+" "+" gain="+getData().getGain()+"<br/>"+
			"treble="+getData().getTreble()+" middle="+getData().getMiddle()+" bass="+getData().getBass()+"<br/>" +
			"presence="+getData().getPresence()+" NR="+getData().getNoiseReduction()+"<br/>"+
			"cabinet "+bool2Str(getData().isCabinetEnabled())+": "+getData().getCabinetName()+"<br/>"+
			"pedal &nbsp;&nbsp;&nbsp;"+bool2Str(getData().isPedalEnabled())+": "+getData().getPedalEffectName()+" edit="+getData().getPedalEdit()+"<br/>"+
			"reverb &nbsp;&nbsp;"+bool2Str(getData().isReverbEnabled())+": "+getData().getReverbEffectName()+" type="+getData().getReverbType()+"<br/>"+
			"delay &nbsp;&nbsp;&nbsp;"+bool2Str(getData().isDelayEnabled())+": "+getData().getDelayEffectName()+"<br/>" +
					"&nbsp; depth="+getData().getDelayDepth()+" feedback="+getData().getDelayFeedback()+" speed="+
					getData().getDelaySpeedString()+
			
			"</html>";
	}

	public WebUser getOwner() {
		return owner;
	}

	public void setOwner(WebUser owner) {
		this.owner = owner;
	}
}
