package net.robig.stlab.model;

import net.robig.net.XmlParser.XmlElement;

public class WebUser {
	String username;
	int id;
	
	static WebUser fromXml(XmlElement userElement) throws InvalidXmlException{
		WebUser wu=new WebUser();
		try {
			wu.username=userElement.getAttribute("username");
			wu.id=Integer.parseInt(userElement.getAttribute("user_id"));
		}catch(Exception ex){
			throw new InvalidXmlException(ex.getMessage());
		}
		return wu;
	}
	
	public String toString() {
		return "User #"+id+" Ó"+username+"Ó";
	}
	
	
	
	public String toHtml(String head) {
		return "<html>" +
			"<b>"+head+"</b><br/>"+
			username+
			
			"</html>";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
