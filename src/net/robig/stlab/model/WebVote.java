package net.robig.stlab.model;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLab;
import net.robig.stlab.gui.controls.WrappableLabel;

public class WebVote extends WrappableLabel {
	/*
webDetailsDescriptionLabel=new JTextPane();
		webDetailsDescriptionLabel.setContentType("text/html");
		webDetailsDescriptionLabel.setMaximumSize(new Dimension(236,9999));
		webDetailsDescriptionLabel.setEditable(false);
		webDetailsDescriptionLabel.setOpaque(false);
	 */
	private static final long serialVersionUID = -4526786765035140537L;
	static DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	WebUser user=null;
	int value=0;
	int id=0;
	int count=0;
	Date created=new Date();
	String comment="";
	private static Logger log=new Logger(WebVote.class);
	
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
			ex.printStackTrace(log.getDebugPrintWriter());
			throw new InvalidXmlException("error parsing vote: "+ex.getMessage());
		}
		return vote;
	}
	
	
	public String toString() {
		return "Vote: Comment: "+comment+" from "+user;
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
