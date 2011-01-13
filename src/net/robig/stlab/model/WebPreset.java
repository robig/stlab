package net.robig.stlab.model;

import java.util.Date;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;

public class WebPreset {
	static Logger log=new Logger(WebPreset.class);
	private int id=0;
	String title="";
	String tags="";
	String description="";
	float voteAvg=0;
	Date created=new Date();
	StPreset preset=null;
	WebUser owner=null;
	public static WebPreset fromXml(XmlElement presetElement) throws InvalidXmlException{
		WebPreset wp=new WebPreset();
		try {
			String title=presetElement.getAttribute("title");
			String data=presetElement.find("data").get(0).getText();
			String desc=presetElement.find("description").get(0).getText();
			long ts_created=Long.parseLong(presetElement.getAttribute("created"));
			wp.created=new Date(ts_created);
			StPreset p=new StPreset();
			p.parseParameters(data);
			p.setName(title);
			wp.title=title;
			wp.description=desc;
			wp.setId(Integer.parseInt(presetElement.getAttribute("id")));
			wp.preset=p;
			XmlElement owner=presetElement.find("owner").get(0);
			wp.owner=WebUser.fromXml(owner);
//			presetElement.find("votes")
			wp.voteAvg=Float.parseFloat(presetElement.find("votes").get(0).getAttribute("avg"));
		}catch(Exception ex){
			log.debug("Exception ocoured parsing XML!");
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

	public static String bool2Str(boolean b){
		return b?"on&nbsp;":"off";
	}
	
	/**
	 * get a detailed html formated description
	 * @return
	 */
	public String toHtml(){
		return "<html><div><b>Description:</b></br>"+
			getDescription()+
			"</div><br/>"+
			"Amp: "+getData().getAmpName()+" ("+getData().getAmpTypeName()+")<br/>"+
			"volume="+getData().getVolume()+" "+" gain="+getData().getGain()+"<br/>"+
			"treble="+getData().getTreble()+" middle="+getData().getMiddle()+" bass="+getData().getBass()+"<br/>" +
			"presence="+getData().getPresence()+" NR="+getData().getNoiseReduction()+"<br/>"+
			"cabinet "+bool2Str(getData().isCabinetEnabled())+": "+getData().getCabinetName()+"<br/>"+
			"pedal &nbsp;&nbsp;&nbsp;"+bool2Str(getData().isPedalEnabled())+": "+getData().getPedalEffectName()+" value="+getData().getPedalEdit()+"<br/>"+
			"reverb &nbsp;&nbsp;"+bool2Str(getData().isReverbEnabled())+": "+getData().getReverbEffectName()+" value="+getData().getReverbEdit()+"<br/>"+
			"delay &nbsp;&nbsp;&nbsp;"+bool2Str(getData().isDelayEnabled())+": "+getData().getDelayEffectName()+" depth="+getData().getDelayDepth()+"<br/>" +
					"&nbsp; feedback="+getData().getDelayFeedback()+" speed="+
					getData().getDelaySpeedString()+
			
			"</html>";
	}
	
	public String toBasicHtml(){
		return "<html>"+
			"<u>Author:</u><br/>"+
			getOwner().getUsername()+
			"<br/>"+
			"<u>Created:</u><br/>"+
			getCreated()+"<br/>"+
			"<u>Votes:</u><br/>"+
			"avg: "+getVoteAvg()+
			"</html>";
			
	}

	public WebUser getOwner() {
		return owner;
	}

	public void setOwner(WebUser owner) {
		this.owner = owner;
	}

	public float getVoteAvg() {
		return voteAvg;
	}

	public void setVoteAvg(int voteAvg) {
		this.voteAvg = voteAvg;
	}
	
	public float getRating(){
		return getVoteAvg();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
