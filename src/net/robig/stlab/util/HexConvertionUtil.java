package net.robig.stlab.util;

public class HexConvertionUtil {
	/** converts any hex string to an byte array
	 * @param hex
	 * @return byte array of the hex values
	 */
	public static byte[] hex2byte(String hex){
		String strMessage=hex.replaceAll(" ", "").toUpperCase();
		int	nLengthInBytes = strMessage.length() / 2;
		byte[]	abMessage = new byte[nLengthInBytes];
		for (int i = 0; i < nLengthInBytes; i++)
		{
			abMessage[i] = (byte) Integer.parseInt(strMessage.substring(i * 2, i * 2 + 2), 16);
		}
		return abMessage;//toHexString(abMessage)
	}
	
	public static int hex2int(String hex){
		if(hex.length()<2) return 0;
		return Integer.parseInt(hex, 16);	
	}
	
	/** convert bytes to its hex string 
	 * @param bytes
	 * @return string of hex values
	 */
	public static String toHexString(byte bytes[])
	{
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i)
		{
			retString.append(
				Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return retString.toString();
	}
	
	/**
	 * Converts a single integer to hex string
	 * @param i
	 * @return
	 */
	public static String toHexString(int i){
		return Integer.toHexString(0x0100 + (i & 0x00FF)).substring(1);
		
	}
	
	public static String toHexString2(int i){
		return Long.toHexString(0x010000 + (i)).substring(1);
	}
	
	public static String toHexString3(int i){
		return Long.toHexString(0x01000000 + (i)).substring(1);
	}
	
	
	public static String formatHexData(byte[] data){
		String fulldata=toHexString(data);
		String ret="";
		int off=0;
		int len=fulldata.length();
		for(off=0;off<len;off+=8){
			int remain=len-off;
			if(remain<=8)
				ret+=fulldata.substring(off);
			else
				ret+=fulldata.substring(off,off+8)+" ";
		}
		return ret;
	}
}
