package com.kssj.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;


/**
* @Description: 字符转换类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:54:20
* @version V1.0
*/
public class StringUtil {
	public static final String numberChar = "0123456789";
	
   /**
    * @method: serial
    * @Description: 流水号生成器
    * 
    * @param len		固定位数
    * @param marginal	传入数字
    * @return String
    * @throws
    */
    public static String serial(int len, int marginal) {
    	
        StringBuffer zeros = new StringBuffer();
        for (int i = 0; i < len; i++) 
        {
            zeros.append("0");
        }
        String temp = String.valueOf(marginal);
        String str = zeros.substring(0, len-temp.length()).concat(temp);
        return str;
    }
	
	/**
	 * 判断某个字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 简单的字符串格式化，性能较好。支持不多于10个占位符，从%1开始计算，数目可变。参数类型可以是字符串、Integer、Object，
	 * 甚至int等基本类型
	 * 、以及null，但只是简单的调用toString()，较复杂的情况用String.format()。每个参数可以在表达式出现多次。
	 * 
	 * @param msgWithFormat
	 * @param autoQuote
	 * @param args
	 * @return
	 */
	public static StringBuilder formatMsg(CharSequence msgWithFormat,
			boolean autoQuote, Object... args) {
		int argsLen = args.length;
		boolean markFound = false;

		StringBuilder sb = new StringBuilder(msgWithFormat);

		if (argsLen > 0) {
			for (int i = 0; i < argsLen; i++) {
				String flag = "%" + (i + 1);
				int idx = sb.indexOf(flag);
				// 支持多次出现、替换的代码
				while (idx >= 0) {
					markFound = true;
					sb.replace(idx, idx + 2, toString(args[i], autoQuote));
					idx = sb.indexOf(flag);
				}
			}

			if (args[argsLen - 1] instanceof Throwable) {
				StringWriter sw = new StringWriter();
				((Throwable) args[argsLen - 1])
						.printStackTrace(new PrintWriter(sw));
				sb.append("\n").append(sw.toString());
			} else if (argsLen == 1 && !markFound) {
				sb.append(args[argsLen - 1].toString());
			}
		}
		return sb;
	}

	public static StringBuilder formatMsg(String msgWithFormat, Object... args) {
		return formatMsg(new StringBuilder(msgWithFormat), true, args);
	}

	public static String toString(Object obj, boolean autoQuote) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("NULL");
		} else {
			if (obj instanceof Object[]) {
				for (int i = 0; i < ((Object[]) obj).length; i++) {
					sb.append(((Object[]) obj)[i]).append(", ");
				}
				if (sb.length() > 0) {
					sb.delete(sb.length() - 2, sb.length());
				}
			} else {
				sb.append(obj.toString());
			}
		}
		if (autoQuote
				&& sb.length() > 0
				&& !((sb.charAt(0) == '[' && sb.charAt(sb.length() - 1) == ']') || (sb
						.charAt(0) == '{' && sb.charAt(sb.length() - 1) == '}'))) {
			sb.insert(0, "[").append("]");
		}
		return sb.toString();
	}

	/**
	 * 把字符串中的带‘与"转成\'与\"
	 * 
	 * @param orgStr
	 * @return
	 */
	public static String convertQuot(String orgStr) {
		return orgStr.replace("'", "\\'").replace("\"", "\\\"");
	}

	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte digest[] = md.digest(inputStr.getBytes("UTF-8"));

			return new String(Base64.encodeBase64(digest));

			// return (new BASE64Encoder()).encode(digest);
			// return new String(Hex.encodeHex(digest));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * HTML实体编码转成普通的编码
	 * 
	 * @param dataStr
	 * @return
	 */
	public static String htmlEntityToString(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			int system = 10;// 进制
			if (start == 0) {
				int t = dataStr.indexOf("&#");
				if (start != t)
					start = t;
			}
			end = dataStr.indexOf(";", start + 2);
			String charStr = "";
			if (end != -1) {
				charStr = dataStr.substring(start + 2, end);
				// 判断进制
				char s = charStr.charAt(0);
				if (s == 'x' || s == 'X') {
					system = 16;
					charStr = charStr.substring(1);
				}
			}
			// 转换
			try {
				char letter = (char) Integer.parseInt(charStr, system);
				buffer.append(new Character(letter).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			// 处理当前unicode字符到下一个unicode字符之间的非unicode字符
			start = dataStr.indexOf("&#", end);
			if (start - end > 1) {
				buffer.append(dataStr.substring(end + 1, start));
			}

			// 处理最后面的非unicode字符
			if (start == -1) {
				int length = dataStr.length();
				if (end + 1 != length) {
					buffer.append(dataStr.substring(end + 1, length));
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 把String转成html实体字符
	 * 
	 * @param str
	 * @return
	 */
	public static String stringToHtmlEntity(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			switch (c) {
			case 0x0A:
				sb.append(c);
				break;

			case '<':
				sb.append("&lt;");
				break;

			case '>':
				sb.append("&gt;");
				break;

			case '&':
				sb.append("&amp;");
				break;

			case '\'':
				sb.append("&apos;");
				break;

			case '"':
				sb.append("&quot;");
				break;

			default:
				if ((c < ' ') || (c > 0x7E)) {
					sb.append("&#x");
					sb.append(Integer.toString(c, 16));
					sb.append(';');
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 字符串 转unicode
	 * @param s
	 * @return
	 */
	public static String stringToUnicode(String s) {
		String unicode = "";
		char[] charAry = new char[s.length()];
		for (int i = 0; i < charAry.length; i++) {
			charAry[i] = (char) s.charAt(i);
			unicode += "\\u" + Integer.toString(charAry[i], 16);
		}
		return unicode;
	}

	/**
	 * unicode转字符串
	 * @param unicodeStr
	 * @return
	 */
	public static String unicodeToString(String unicodeStr) {
		StringBuffer sb = new StringBuffer();
		String str[] = unicodeStr.toUpperCase().split("\\\\U");
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(""))
				continue;
			char c = (char) Integer.parseInt(str[i].trim(), 16);
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 转换html
	 * @param inputString
	 * @return
	 */
	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style>]*?>[\s\S]*?<\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = java.util.regex.Pattern.compile(regEx_script,
					java.util.regex.Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = java.util.regex.Pattern.compile(regEx_style,
					java.util.regex.Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = java.util.regex.Pattern.compile(regEx_html,
					java.util.regex.Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	/**
	 * 获取8位随机字符串
	 * @return
	 */
	public static String get8rands() {
		Random rd = new Random(); // 創建隨機對象
		String n = ""; // 保存隨機數
		int rdGet; // 取得隨機數
		do {
			if (rd.nextInt() % 2 == 1) {
				rdGet = Math.abs(rd.nextInt()) % 10 + 48; // 產生48到57的隨機數(0-9的鍵位值)
			} else {
				rdGet = Math.abs(rd.nextInt()) % 26 + 97; // 產生97到122的隨機數(a-z的鍵位值)
			}
			char num1 = (char) rdGet; // int轉換char
			String dd = Character.toString(num1);
			n += dd;

		} while (n.length() < 8);// 設定長度，此處假定長度小於8

		return n;
	}
	
	//字符串是否空
	public static boolean isEmpty(String str){
		return (str == null || str.toString().trim().equals(""));
	}
	//字符串是否不空
	public static boolean isNotEmpty(String str){
		return (str !=null && !str.toString().trim().equals(""));
	}
	
	/**
	 * 将为null的字符串转为空字符串 将不为null的字符串trim()
	 * @param target  目标字符串
	 * @return 
	 */
	public static String getNotNullString(String target){
		if(target == null) {
			return "";
		} else {
			return target.trim();
		}
	}
	
	/**
	 * 生成定长的随机数
	 * @param length 随机数长度
	 * @return
	 */
	public static String generateString(int length) {		 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) { 
			sb.append(numberChar.charAt(random.nextInt(numberChar.length()))); 
		} 
		return sb.toString(); 
	} 
	
	/**
	 * 生成新的文件名，格式为 原文件名+日期字符串+随机数
	 * @param fileName
	 * @param length 随机数长度
	 * @return
	 */
	public static String getDateStrAndRandomString(int length){
		Date date = new Date();
		String dateStr = DateUtil.getDateString(date, "ddHHmmss");
		String randomStr = generateString(length);
		String newFileName = dateStr + randomStr;
		return newFileName;
	}
	
	/**
	 * object 转 string
	 */
	public static String getObjToString (Object obj){
		String str=null;
		if(obj!=null){
			str = obj.toString();
		}
		return str;
	}
	
//	public static void main(String[] args) {
//		// String password = "未命名任务";
//		// String result = stringToHtmlEntity(password);
//
//		// String str="&#x672a;&#x547d;&#x540d;&#x4efb;&#x52a1;";
//		// String result=htmlEntityToString(str);
//
//		// byte[]dis={-10, -32, -95, -30, -84, 65, -108, 90, -102, -89, -1,
//		// -118, -118, -86, 12, -21, -63, 42, 59, -52, -104, 26, -110, -102,
//		// -43, -49, -127, 10, 9, 14, 17, -82};
//		// System.out.println("array:"+ new String(Hex.encodeHex(dis)));
//		// System.out.println("result:" + result);
//
//		// String content="pd6717864949166496642.40.连线2";
//		// String encode=stringToUnicode(content);
////		String vm = "123";
////		System.out.println(StringUtil.get8rands());
////		String str = "[{queueID:\"123\",fileCode:\"2131\"},{queueID:\"123\",fileCode:\"1231\"}]";
////		System.out.println(StringUtil.unicodeToString(stringToUnicode(str)));
//			
//		// System.out.println("another:" + unicodeToString(encode));
//		String unicodeStr = "\r\n\\u5b\\u7b\\u64\\u72\\u77\\u67\\u4e\\u6f\\u3a\\u22\\u73\\u64\\u66\\u73\\u64\\u66\\u22\\u2c\\u76\\u65\\u72\\u73\\u69\\u6f\\u6e\\u4e\\u6f\\u3a\\u22\\u64\\u66\\u73\\u64\\u66\\u73\\u64\\u66\\u22\\u2c\\u64\\u72\\u77\\u67\\u4e\\u61\\u6d\\u65\\u3a\\u22\\u73\\u64\\u66\\u73\\u64\\u66\\u22\\u7d\\u5d";
//		System.out.println(StringUtil.unicodeToString(unicodeStr));
//	}
}
