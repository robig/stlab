package net.robig.net;

import java.util.regex.Pattern;
import net.robig.logging.Logger;

public class LinkValidator {
	
	Logger log = new Logger(LinkValidator.class);
	
	static Pattern urlPattern = Pattern.compile(
			"^(https?://)"
	        + "(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //user
	        + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP- 199.194.52.184
	        + "|" // allows either IP or domain
	        + "([0-9a-z_!~*'()-]+\\.)*" // tertiary domain(s)- www.
	        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // second level domain
	        + "[a-z]{2,6})" // first level domain- .com or .museum
	        + "(:[0-9]{1,4})?" // port number- :80
	        + "((/?)|" // a slash isn't required if there is no file name
	        + "(/[0-9A-Za-z_!~*'().;?:@&=+$,%#-]+)+/?)"
	        + "(\\?[a-zA-Z0-9=&_-]+)?" // GET parameters
	        + "$");
	
	static Pattern emailPattern = Pattern.compile(
			"^[a-zA-Z0-9_-]+@(([0-9]{1,3}" +
            "\\.[0-9]{1,3}.[0-9]{1,3}\\.)|(([a-zA-Z0-9-]+" +
            "\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$");
	
	static Pattern youtubePattern = Pattern.compile("^http://www\\.youtube\\.com/watch\\?[A-Za-z0-9=_]*v=[0-9A-Za-z]+.*");
	
	public static boolean validateLinkSyntax(String url){
		return isUrl(url);
	}
	
	public static boolean validateLink(String url){
		return isUrl(url);
	}
	
	public static boolean isYoutube(String url) {
		if(!isUrl(url)) return false;
		return youtubePattern.matcher(url).matches();
	}

    public static boolean isEmail(String Email){
        return emailPattern.matcher(Email).matches();
    }

    public static boolean isUrl(String url){
        return urlPattern.matcher(url).matches();
    }
    
    public static String autocorrectUrl(String in) throws InvalidLinkException{
    	if(isUrl(in)) return in;
    	if(!in.startsWith("http://") && !in.startsWith("https://")){
    		return autocorrectUrl("http://"+in);
    	}
    	throw new InvalidLinkException("Link invalid: "+in);
    }
	
    public static boolean urlAccessible(String url){
    	HttpRequest http = new HttpRequest(url);
    	http.requestHtml();
    	return http.isSent();
    }
}
