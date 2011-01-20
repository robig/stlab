package net.robig.stlab.model;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLab;

public class WebVote extends JLabel{
	private static final long serialVersionUID = -4526786765035140537L;
	WebUser user=null;
	int value=0;
	String comment="";
	
	public static WebVote fromXml(XmlElement voteElement) throws InvalidXmlException{
		WebVote vote=new WebVote();
		try {
			vote.value=Integer.parseInt(voteElement.getAttribute("value"));
			vote.comment=voteElement.getAttribute("comment");
			vote.user=WebUser.fromXml(voteElement.find("user").get(0));
			vote.initialize();
		}catch(Exception ex){
			throw new InvalidXmlException(ex.getMessage());
		}
		return vote;
	}
	
	
	public String toString() {
		return "Vote: Comment:Ó"+comment+"Ó from "+user;
	}
	
	public void initialize(){
		setText(toHtml());
		TitledBorder b=BorderFactory.createTitledBorder(user.getUsername()+" voted "+value+" wrote:");
		b.setTitleColor(StLab.FOREGROUND);
		setBorder(b);
	}
	
	public String toHtml() {
		return "<html>"+
			"<b>Vote: "+value+" by "+user.getUsername()+" wrote:</b><br/>"+
			comment+
			"<br/>"+
			"</html>";
	}

}
