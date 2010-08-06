package net.robig.stlab.util;

public class StringUtil {
	/**
	 * format a String array for outputing
	 * @param arr
	 * @return
	 */
	public static String array2String(String[] arr) {
		if(arr==null) return "NULL";
		if(arr.length==0) return "[]";
		StringBuffer buf=new StringBuffer();
		buf.append("[ ");
		for(int i=0;i<arr.length-1;i++){
			buf.append(arr[i]);
			buf.append(", ");
		}
		buf.append(arr[arr.length-1]);
		buf.append(" ]");
		return buf.toString();
	}
}
