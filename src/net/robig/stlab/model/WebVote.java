package net.robig.stlab.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLab;

public class WebVote extends JLabel{
	private static final long serialVersionUID = -4526786765035140537L;
	static DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	WebUser user=null;
	int value=0;
	int id=0;
	int count=0;
	Date created=new Date();
	String comment="";
	
	public static WebVote fromXml(XmlElement voteElement) throws InvalidXmlException{
		WebVote vote=new WebVote();
		try {
			vote.id=Integer.parseInt(voteElement.getAttribute("vote_id"));
			vote.value=Integer.parseInt(voteElement.getAttribute("value"));
			vote.count=Integer.parseInt(voteElement.getAttribute("count"));
			vote.comment=voteElement.getAttribute("comment");
			vote.user=WebUser.fromXml(voteElement.find("user").get(0));
			try {
				vote.created=DateFormat.getDateInstance().parse(voteElement.getAttribute("javatime_created"));
			} catch(ParseException ex){
				long ts_created=Long.parseLong(voteElement.getAttribute("ts_created"));
				vote.created=new Date(ts_created*1000); //expected in milliseconds
			}
			vote.initialize();
		}catch(Exception ex){
			throw new InvalidXmlException("error parsing vote: "+ex.getMessage());
		}
		return vote;
	}
	
	
	public String toString() {
		return "Vote: Comment:Ó"+comment+"Ó from "+user;
	}
	
	public void initialize(){
		setText(toHtml());
		TitledBorder b=BorderFactory.createTitledBorder(getTitle());
		b.setTitleColor(StLab.FOREGROUND);
		setBorder(b);
	}
	
	public String getTitle(){
		return "#"+count+" "+user.getUsername()+" voted at "+getCreatedFormated()+":";
	}
	
	public String toHtml() {
		return "<html>"+
			"<b>Voted "+value+" stars and wrote:</b><br/>"+
			comment+
			"<br/>"+
			"</html>";
	}

	public Date getCreated() {
		return created;
	}
	
	public String getCreatedFormated(){
		return formatter.format(getCreated());
	}
}
