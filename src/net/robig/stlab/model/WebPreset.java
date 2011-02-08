package net.robig.stlab.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;

public class WebPreset {
	static DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	static Logger log=new Logger(WebPreset.class);
	private int id=0;
	String title="";
	String tags="";
	String description="";
	float voteAvg=0;
	int voteCount=0;
	int voteSum=0;
	boolean alreadyVoted=false;
	Date voted=null;
	Date created=new Date();
	StPreset preset=null;
	WebUser owner=null;
	List<WebVote> votes=new ArrayList<WebVote>();
	String link=null;
	
	public static WebPreset fromXml(XmlElement presetElement) throws InvalidXmlException{
		WebPreset wp=new WebPreset();
		try {
			String title=null;
			String data=null;
			try {
				wp.setId(Integer.parseInt(presetElement.getAttribute("id")));
			}catch(Exception ex){
				log.debug("Error parsing XML: id "+ex);
				throw new InvalidXmlException("Error parsing XML id not found: "+ex+" "+ex.getMessage());
			}
			try {
				title=presetElement.getAttribute("title");
				assert title!=null;
			}catch(Exception ex){
				log.debug("Error parsing XML: title attribute not found "+ex);
				throw new InvalidXmlException("Error parsing XML: title attribute not found: "+ex+" "+ex.getMessage());
			}
			try {
				data=presetElement.find("data").get(0).getText();
			}catch(Exception ex){
				log.debug("Error parsing XML: preset data element "+ex);
				throw new InvalidXmlException("Error parsing XML: preset data not found: "+ex+" "+ex.getMessage());
			}
			try{
				wp.description=presetElement.find("description").get(0).getText();
			}catch(Exception ex){
				log.debug("Error parsing XML: description element "+ex);
				throw new InvalidXmlException("Error parsing XML: description element not found: "+ex+" "+ex.getMessage());
			}
			
			try{
				wp.link=presetElement.find("link").get(0).getText();
			}catch(Exception ex){
				log.debug("Error parsing XML: link element "+ex);
				throw new InvalidXmlException("Error parsing XML: link element not found: "+ex+" "+ex.getMessage());
			}
			
			try{// presetElement
				wp.tags=presetElement.find("tags").get(0).getText().replace("\r","");
			}catch(Exception ex){
				log.debug("Error parsing XML: link element "+ex);
				throw new InvalidXmlException("Error parsing XML: link element not found: "+ex+" "+ex.getMessage());
			}
			
			try {
				try {
					wp.created=DateFormat.getDateInstance().parse(presetElement.getAttribute("javatime_created"));
				} catch(ParseException ex){
					long ts_created=Long.parseLong(presetElement.getAttribute("created"));
					wp.created=new Date(ts_created*1000); //expected in milliseconds
				}
			}catch(Exception ex){
				log.debug("Error parsing XML: created date attribute "+ex);
				throw new InvalidXmlException("Error parsing XML: date attribute not found: "+ex+" "+ex.getMessage());
			}
			StPreset p=new StPreset();
			p.parseParameters(data);
			p.setTitle(title);
			wp.title=title;
			wp.preset=p;
			try {
				XmlElement owner=presetElement.find("owner").get(0);
				wp.owner=WebUser.fromXml(owner);
			}catch(Exception ex){
				log.debug("Error parsing XML: preset owner element! "+ex);
				throw new InvalidXmlException("Error parsing XML: preset owner element not found! "+ex+" "+ex.getMessage());
			}
			XmlElement votesElement=null;
			try {
				votesElement=presetElement.find("votes").get(0);
			}catch(Exception ex){
				log.debug("Error parsing XML: cannot find votes element");
				throw new InvalidXmlException("Error parsing vote avg in XML: "+ex+" "+ex.getMessage());
			}
			try {
				wp.voteAvg=Float.parseFloat(votesElement.getAttribute("avg"));
			}catch(Exception ex){
				log.debug("Error parsing XML: vote avg attribute not found! "+ex);
				throw new InvalidXmlException("Error parsing XML: vote avg attibute not found! "+ex+" "+ex.getMessage());
			}
			try {
				wp.voteSum=Integer.parseInt(votesElement.getAttribute("sum"));
			}catch(Exception ex){
				log.debug("Error parsing XML: vote sum attribute not found! "+ex);
//				throw new InvalidXmlException("Error parsing XML: vote sum attibute not found! "+ex+" "+ex.getMessage());
			}
			try {
				wp.voteCount=Integer.parseInt(votesElement.getAttribute("count"));
			}catch(Exception ex){
				log.debug("Error parsing XML: vote count attribute not found! "+ex);
				throw new InvalidXmlException("Error parsing XML: vote count attibute not found! "+ex+" "+ex.getMessage());
			}
			try {
				long voted=Long.parseLong(votesElement.getAttribute("voted"));
				wp.alreadyVoted=voted>0;
				wp.voted=new Date(voted*1000);
			}catch(Exception ex){
				log.debug("Error parsing XML: vote date attribute not found! "+ex);
				throw new InvalidXmlException("Error parsing XML: vote date attibute not found! "+ex+" "+ex.getMessage());
			}
			try {
				if(wp.voteCount>0){
					for(XmlElement e: votesElement.find("vote")){
						WebVote vote=WebVote.fromXml(e);
						log.debug(vote.toString());
						wp.votes.add(vote);
					}
					log.debug("Added "+wp.votes.size()+" votes"); // should be "+presetElement.find("vote").size());
				}
			}catch(Exception ex){
				log.debug("Error parsing XML: failed to get attached votes! "+ex);
				throw new InvalidXmlException("Error parsing XML: failed to get attached votes! "+ex+" "+ex.getMessage());
			}
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
		return "<html><u>Description:</u><br/>"+
			getDescription()+
			"<br/>"+
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
	
	private String format(String in){
		in.replace("\r", "");
		if(in.endsWith("\n"))in=in.substring(0,in.length()-2);
		return in.replace("\n","<br/>");
	}
	
	private String getTagsHtml(){
		if(!hasTags()) return "";
		return "<br/><u>Tags:</u><br/>"+
			format(getTags())+"<br/>";
	}
	
	private String getDescriptionHtml(){
		if(description.length()<1) return "";
		return "<br/><u>Description:</u><br/>"+
			format(getDescription())+"<br/>";
	}
	
	public String toBasicHtml(boolean isLoggedin){
		return "<html>"+
			getLink()+"<br/>"+
			getTagsHtml()+
			"<u>Author:</u><br/>"+
			getOwner().getUsername()+"<br/>"+
			"<u>Created:</u><br/>"+
			formatter.format(getCreated())+"<br/>"+
			"<u>Votes:</u><br/>"+
			"rating: &nbsp;"+getVoteAvg()+"<br/>"+
			"total votes: "+getVoteCount()+"<br/>"+
			(isLoggedin?
					(hasAlreadyVoted()?"voted already on<br/>"+formatter.format(getVoted()):"not voted yet"):""
			)+
			"</html>";
			
	}
	
	public String toTopPanelHtml(boolean isLoggedin){
		return "<html>"+
			"#"+getId()+" <b>"+getTitle()+"</b><br/>"+
			"by: "+getOwner().getUsername()+"<br/>"+
			getDescriptionHtml()+
			getTagsHtml()+
			"<br/><u>Created:</u><br/>"+
			getCreatedFormated()+"<br/>"+
//			getCreated()+"<br/>"+
			"</html>";
			
	}
	
	public String getTopPanelVotesHtml(boolean isLoggedin){
		return "<html><br/><u>Votes:</u><br/>"+
		(getVoteCount()>0?
				"Rating: &nbsp;"+getVoteAvg()+" by "+getVoteCount()+" votes<br/>":
				"No votes yet"
		)+
		(isLoggedin?
				(hasAlreadyVoted()?"voted already at<br/>"+formatter.format(getVoted()):"not voted yet"):""
		)+
		"<br/></html>";
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
	
	public String getCreatedFormated(){
		return formatter.format(getCreated());
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getVoteSum() {
		return voteSum;
	}

	public void setVoteSum(int voteSum) {
		this.voteSum = voteSum;
	}

	public boolean hasAlreadyVoted() {
		return alreadyVoted;
	}
	
	public void setAlreadyVoted(boolean v) {
		alreadyVoted=v;
		if(v)voted=new Date();
	}

	public Date getVoted() {
		return voted;
	}

	public void setVoteAvg(float voteAvg) {
		this.voteAvg = voteAvg;
	}

	public List<WebVote> getVotes() {
		return votes;
	}

	public void setVotes(List<WebVote> votes) {
		this.votes = votes;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public boolean hasLink(){
		return this.link!=null && this.link.length()>0;
	}
	
	public boolean hasTags(){
		return tags.length()>0;
	}
}
