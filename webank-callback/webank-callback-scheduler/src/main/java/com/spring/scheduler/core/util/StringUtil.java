package com.spring.scheduler.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 当前版本的StringUtil中删除了大量无用函数。
 * 其中有些方法只是被移动到更适当的地方，如ArrayUtil, DbUtil。
 * 另外也有很多方法只是改了个更适当名字，并根据含义适当拓展。
 * PS 暂时在StringUtilEx 保留了StringUtil 之前的内容
 */
public class StringUtil {
	
	/**
	 * return true if the string is null or length=0
	 */
	private static boolean isNil(String str){
		return str == null || str.length() == 0;
	}
	public static String getId(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	/**
	 * 判断是否是空字符串<br/>
	 * null,"","null" 都是空
	 */
	public static boolean isEmpty(String s) {
		return s == null || (s = s.trim()).equals("") || s.equals("null");
	}

	/**
	 * 判断对象是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(Object str) {
		if(str == null || "".equals(str)) return false;
		if(str.toString().length()>0) return true;
		return false;
	}
	
	/**
	 * 首字母大写
	 */
	public static String firstUpperCase(String src) {
		return capitalize(src);
	}
	
	public static String capitalize(String src){
		if(isNil(src)) return src;
		return Character.toTitleCase(src.charAt(0)) + src.substring(1);
	}

	/**
	 * 判断是否与给定字符串样式匹配
	 * @param str 字符串
	 * @param pattern 正则表达式样式
	 */
	public static boolean match(String src, String pattern) {
		if(pattern == null) return false;
		return match(src, Pattern.compile(pattern));
	}
	
	public static boolean match(String src, Pattern p){
		if(isNil(src) || p == null) return false;
		return p.matcher(src).matches();
	}

	/**
	 * 判断是否纯字母组合
	 */
	public static boolean isABC(String src) {
		return match(src, abcPattern);
	}
	private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
	
	/**
	 * 判断是否数字表示
	 */
	public static boolean isNumeric(String src) {
		return match(src, numericPattern);
	}
	private static Pattern numericPattern = Pattern.compile("^\\-?[0-9]+$");

	/**
	 * 判断是否浮点数字表示
	 */
	public static boolean isFloatNumeric(String src) {
		return match(src, floatNumericPattern);
	}
	private static Pattern floatNumericPattern = Pattern.compile("^\\-?[0-9]+[\\.]?[0-9]*$");

	/**
	 *  判断一个字符串是否都为数字0-9
	 */
	public static boolean isDigit(String o){
		if(isEmpty(o)) return false;
		for(int i = 0; i<o.length(); i++){
			char c = o.charAt(i);
			if(c<'0') return false;
			if(c>'9') return false;
		}
		return true;
	}
	
	/**
	 * 去掉两端的指定字符
	 * @param src
	 * @param chars 指定的字符集合
	 * @return
	 */
	public static String trim(String src, String chars){
		int b=0, e=src.length()-1;
		while(b<=e&&chars.indexOf(src.charAt(b))>=0) b++;
		while(e>b&&chars.indexOf(src.charAt(e))>=0) e--;
		if(e<b) return "";
		return src.substring(b,e+1);
	}
	
	public static String concat(String... pieces){
		if(pieces.length<=0) return "";
		if(pieces.length==1) return pieces[0];
		StringBuilder rs = new StringBuilder();
		for (String s : pieces) if (s != null) rs.append(s);
		return rs.toString();
	}

	/**
	 * 用","连接数组为字符串
	 * @param array
	 * @param symbol
	 * @return
	 */
	public static <T> String joinString(T[] array){
		return joinString(array, ",");
	}
	
	/**
	 * 用","连接数组为字符串
	 * @param array
	 * @param symbol
	 * @return
	 */
	public static <T> String joinString(Iterable<T> array){
		return joinString(array, ",");
	}
	
	/**
	 * 用连接符号连接数组为字符串
	 * @param array
	 * @param symbol
	 * @return
	 */
	public static <T> String joinString(T[] array, String symbol) {
		if(array==null || array.length<=0) return "";
		StringBuilder rs = new StringBuilder();
		for (Object s : array)
			if (s != null && s.toString().trim().length() > 0)
				rs.append(s).append(symbol);
		if (rs.length() >= symbol.length())
			rs.setLength(rs.length() - symbol.length());
		return rs.toString();
	}

	/**
	 * 用连接符号连接数组为字符串
	 * @param array
	 * @param symbol
	 * @return
	 */
	public static String joinString(Iterable<?> array, String joiner) {
		if(array==null || !array.iterator().hasNext()) return "";
		StringBuilder rs = new StringBuilder();
		for (Object s : array)
			if (s != null && s.toString().trim().length() > 0)
				rs.append(s).append(joiner);
		if (rs.length() >= joiner.length())
			rs.setLength(rs.length() - joiner.length());
		return rs.toString();
	}
	
	/**
	 * 把数组转换为加上单引号的参数组，1,2,3 => '1','2','3'
	 * @param array
	 * @return 'A','B','C'
	 */
	public static <T> String joinStringPara(T[] array) {
		return joinStringPara("'",array,"'",",");
	}

	/**
	 * 把数组转换为加上单引号的参数组，1,2,3 => '1','2','3'
	 * @param array
	 * @return 'A','B','C'
	 */
	public static <T> String joinStringPara(Iterable<T> array) {
		return joinStringPara("'",array,"'",",");
	}
	
	/**
	 * 把数组转换为带指定前缀、后缀、分隔符的字符串, 如 [1,2,3] => "'1','2','3'"
	 * @param <T>
	 * @param lQuot 左字符串 如"'"
	 * @param array 数组
	 * @param rQuot 右字符串 如"'"
	 * @param joiner 连接字符串 如","
	 * @return
	 */
	public static <T> String joinStringPara(String lQuot, T[] array, String rQuot, String joiner){
		if(array == null || array.length <= 0) return "";
		StringBuilder rs = new StringBuilder(lQuot);
		String sp = rQuot + joiner + lQuot;
		for(T s : array){
			if (s != null && s.toString().trim().length() > 0)
				rs.append(s).append(sp);
		}
		if(rs.length() >= sp.length())
			rs.setLength(rs.length() - sp.length());
		return rs.append(rQuot).toString();
	}
	
	/**
	 * 把数组转换为带指定前缀、后缀、分隔符的字符串, 如 [1,2,3] => "'1','2','3'"
	 * @param <T>
	 * @param lQuot 左字符串 如"'"
	 * @param array 数组
	 * @param rQuot 右字符串 如"'"
	 * @param joiner 连接字符串 如","
	 * @return
	 */
	public static <T> String joinStringPara(String lQuot, Iterable<T> array, String rQuot, String joiner){
		if(array == null || !array.iterator().hasNext()) return "";
		StringBuilder rs = new StringBuilder(lQuot);
		String sp = rQuot + joiner + lQuot;
		for(T s : array){
			if (s != null && s.toString().trim().length() > 0)
				rs.append(s).append(sp);
		}
		if(rs.length() >= sp.length())
			rs.setLength(rs.length() - sp.length());
		return rs.append(rQuot).toString();
	}
	
	/**
	 * 为参数组加上单引号，1,2,3 => '1','2','3'
	 */
	public static String strToLocation(String recordStr) {
		StringBuilder buf = new StringBuilder();
		StringTokenizer location = new StringTokenizer(recordStr, " ,");
		while (location.hasMoreTokens()) {
			buf.append("'");
			buf.append(location.nextToken());
			buf.append("',");
		}
		buf.setLength(buf.length()-1);
		return buf.toString();
	}
	
	/**
	 * 把数组转换为加上单引号的参数组，1,2,3 => '1','2','3'
	 */
	public static String listToLocation(List<?> list){
		if(list != null && list.size() > 0){
			StringBuilder buf = new StringBuilder();
			for(Object obj:list){
				buf.append("'");
				buf.append(obj);
				buf.append("',");
			}
			buf.setLength(buf.length()-1);
			return buf.toString();
		}
		return "";
	}

	/**
	 * 取得字符串的实际长度（考虑了汉字的情况）
	 * 
	 * @param SrcStr
	 *            源字符串
	 * @return 字符串的实际长度
	 */
	public static int getStringLen(String SrcStr) {
		int return_value = 0;
		if (SrcStr != null) {
			char[] theChars = SrcStr.toCharArray();
			for (int i = 0; i < theChars.length; i++) {
				return_value += (theChars[i] <= 255) ? 1 : 2;
			}
		}
		return return_value;
	}	
	
	/**
     * 补齐不足长度
     * @param length 长度
     * @param number 数字
     * @return
     */
    public static String complement(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
    
    /**
     * 补齐不足长度
     * @param length 长度
     * @param number 数字
     * @return
     */
    public static String complement(int length, Long number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }

	/**
	 * 获取从start开始用*替换len个字符
	 * 
	 * @param str 要替换的字符串
	 * @param start 开始位置
	 * @param len 长度
	 * @return 替换后的字符串
	 */
	public static String getMaskedString(String str, int start, int len) {
		if (StringUtil.isEmpty(str)) return str;
		if (str.length() < start) return str;
		int end = (start+len > str.length())? str.length(): start+len;
		char[] c = str.toCharArray();
		for(int i = start; i<end; i++) c[i] = '*';
		return new String(c);
	}

	/**
	 * "abc@cba.bac" => "***@cba.bac"
	 * @param email
	 * @return
	 */
	public static String maskEmailAdress(String email) {
		if (null != email) {
			int index = email.lastIndexOf('@');
			if (index >= 0) {
				email = repeat("*", index+1).concat(email.substring(index));
			}
		}
		return email;
	}

	/**
	 * repeat("*", 6) = "******"
	 */
	public static String repeat(String src, int num) {
		StringBuilder s = new StringBuilder();
		while (--num >= 0)
			s.append(src);
		return s.toString();
	}
	
	/**
	 * 普通的分割字符串为列表，不通过正则表达式，以提高速度
	 * @param s
	 * @param sep 分隔符，非正则表达式
	 * @return
	 */
	public static String[] split(String src, String sep){
		if(isEmpty(src)) return new String[0];
		List<String> r = new ArrayList<String>();
		int lastIndex = -1;
		int index = src.indexOf(sep);
		if (-1 == index && src != null) {
			r.add(src);
			return r.toArray(new String[r.size()]);
		}
		while (index >= 0) {
			if (index > lastIndex) {
				r.add(src.substring(lastIndex + 1, index));
			} else {
				r.add("");
			}

			lastIndex = index;
			index = src.indexOf(sep, index + 1);
			if (index == -1) {
				r.add(src.substring(lastIndex + 1, src.length()));
			}
		}
		return r.toArray(new String[r.size()]);
	}
	
	/**
	 * 以 "," 分割字符串
	 */
	public static String[] split(String src){
		return split(src,",");
	}

	/**
	 * 数组中是否包含指定内容
	 * @param arr 
	 * @param target 包含内容
	 * @return
	 */
	public static boolean contains(String[] arr, String target) {
		if (arr == null || target == null || arr.length == 0) return false;
		for (String str : arr) {
			if (target.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsIgnoreCase(String[] arr, String target){
		if (arr == null || target == null || arr.length == 0) return false;
		for (String str : arr) {
			if (target.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
	 * 
	 * @param sou
	 * @param find
	 * @return
	 */
	public static boolean inStrings(String sou, String... finds) {
		if (!isNil(sou) && finds != null && finds.length > 0) {
			for (String find : finds) {
				if (sou.indexOf(find) > -1)
					return true;
			}
		}
		return false;
	}

	public static boolean inStrings(String sou, List<String> finds) {
		if (!isNil(sou) && finds != null && finds.size() > 0) {
			for (String s : finds) {
				if (sou.indexOf(s) > -1)
					return true;
			}
		}
		return false;
	}

	public static boolean inStrings(String sou, String finds) {
		return inStrings(sou, split(finds));
	}

	/**
	 * 判断两个字符串是否相等<br/>
	 * Empty == null || "";<br/>
	 * equals(Empty, Empty) = true;<br/>
	 * equals(Empty, NotEmpty) = false;<br/>
	 * equals(s1, s2) = s1.equals(s2)
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equals(String s1, String s2) {
		if(isNil(s1)) return isNil(s2);
		return s1.equals(s2);
	}

	/**
	 * 编码转换
	 * @param s
	 * @param srcCode 当前编码
	 * @param desCode 目标编码
	 * @return
	 */
	public static String recode(String s, String srcCode, String desCode){
		if(isNil(s)) return "";
		try{
			return new String(s.getBytes(srcCode), desCode);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
	public static String recode(String s, String desCode){
		if(isNil(s)) return "";
		try{
			return new String(s.getBytes(), desCode);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static String getUTF8Str(String s) {
		return recode(s,"UTF-8");
	}

	/**
	 * 根据正则表达式提取字符串,相同的字符串只返回一个
	 * @param str 源字符串
	 * @param pattern 正则表达式
	 * @return 目标字符串数组
	 */
	public static String[] getMatches(String str, String pattern) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(str);
		if(!matcher.matches()) return null;
		Set<String> result = new HashSet<String>();
		for(int i = 0; i < matcher.groupCount(); i++)
			result.add(matcher.group());
		return result.toArray(new String[result.size()]);
	}
	
	/**
	 * 页面中去除字符串中的空格、回车、换行符、制表符 update by hd 2015/6/16：
	 * 将查询条件由任意空格改为至少一个空格，最后是以一个空格来替换，而不是一个空字符。
	 * @author shazao
	 * @date 2007-08-17
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		if(str == null) return null;
		Matcher m = blankPattern.matcher(str);
		return m.replaceAll(" ");
	}
	private static Pattern blankPattern = Pattern.compile("\\s+|\t|\r|\n");

	/**
	 * 截取sb, se之间的字符串
	 * @param s 源字符串
	 * @param sb 取在sb
	 * @param se 于se
	 * @return 之间的字符串
	 */
	public static String subStrBetween(String s, String sb, String se) {
		if (isNil(s)) return "";
		int b = s.indexOf(sb), e = s.indexOf(se, b);
		if(b<0) b=0; else b = b + sb.length();
		if(e<0) e=s.length();
		return s.substring(b, e);
	}

	/**
	 * URL编码
	 */
	public static String URLEncode(String src, String charset) {
		if(src == null) return "";
		try {
			return URLEncoder.encode(src, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return src;
		}
	}
	
	/**
	 * URL解码
	 */
	public static String URLDecode(String src, String charset){
		if(src == null) return "";
		try {
			return URLDecoder.decode(src, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return src;
		}
	}
	
	/**
	 * 转换如 &#1234;&#1234; 这样的文字
	 */
	public static String parseCodepoints(String str){
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("&#\\d+;");
		Matcher m = p.matcher(str);
		while (m.find()) {
			String old = m.group();
			String dest = str.substring(2, old.length() - 1);
			char ch = (char) Integer.parseInt(dest);
			m.appendReplacement(sb, "" + ch);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 省略。 将长字符串截取为 abcde...
	 * @param str 字符串
	 * @param size 目标长度
	 * @return 字符串，总长度为size，包括省略号。
	 */
	public static String ellipse(String str, int size) {
		if(str == null) return str;
		if(str.length()<=size) return str;
		return str.substring(1, size-1) + "…";
	}

	/**
	 * 判断是否是空字符串 null和"" null返回result,否则返回字符串
	 */
	public static String nvl(String str, String cand0, String... cands) {
		if(isNotEmpty(str)) return str;
		if(isNotEmpty(cand0) || cands.length == 0) return cand0;
		for(String s : cands){
			if(isNotEmpty(s)) return s;
		}
		return "";
	}

	/**
	 * 全角字符变半角字符
	 * 
	 * @author shazao
	 * @date 2008-04-03
	 * @param str
	 * @return
	 */
	public static String full2Half(String str) {
		if (str == null || "".equals(str))
			return "";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c >= 65281 && c < 65373)
				sb.append((char) (c - 65248));
			else
				sb.append(str.charAt(i));
		}

		return sb.toString();

	}

	// 截取数字
	public String getNumbers(String content) {
		Matcher matcher = getNumberPattern.matcher(content);
		if(matcher.find()) return matcher.group(0);
		return "";
	}
	private static Pattern getNumberPattern = Pattern.compile("\\d+");

	// 截取非数字
	public String splitNotNumber(String content) {
		Matcher matcher = getNotNumberPattern.matcher(content);
		if(matcher.find()) return matcher.group(0);
		return "";
	}
	private static Pattern getNotNumberPattern = Pattern.compile("\\D+");

	/**
	 * 如果value是null，返回""
	 */
	public static String parseStr(Object value) {
		return value==null?"":value.toString();
	}

	/**
	 * 左补齐
	 * @param s 待补齐字符串
	 * @param len 总长度
	 * @param pad 填充字符
	 * @return
	 */
	public static String lpad(String s, int len, char pad){
		if(s.length() >= len) return s;
		len -= s.length();
		StringBuilder sb = new StringBuilder();
		while(--len >= 0){
			sb.append(pad);
		}
		sb.append(s);
		return sb.toString();
	}
	
	/**
	 * 右补齐
	 * @param s 待补齐字符串
	 * @param len 总长度
	 * @param pad 填充字符
	 * @return
	 */
	public static String rpad(String s, int len, char pad){
		if(s.length() >= len) return s;
		len -= s.length();
		StringBuilder sb = new StringBuilder(s);
		while(--len >= 0){
			sb.append(pad);
		}
		return sb.toString();
	}
	
	/**
	 * 根据分隔符由根追溯每个节点的路径。
	 * 比如: tracePath("A_1_2_3", "_") = ["A", "A_1", "A_1_2", "A_1_2_3"];
	 * liuch
	 */
	public static String[] tracePath(String str, String spliter) {
		if(str == null) return null;
		List<String> strs = new ArrayList<String>();
		int lastPos = 0;
		for(int i=0; i<str.length(); i++){
			int pos = str.indexOf(spliter, i);
			if(pos>lastPos+spliter.length()-1){
				strs.add(str.substring(0,pos));
				i = pos + spliter.length()-1;
			}
			if(pos<0) strs.add(str);

			lastPos = pos + spliter.length();
		}
		return strs.toArray(new String[strs.size()]);
	}
	
	/**
	 * 根据分隔符计算路径上的节点数
	 */
	public static int getPathLevel(String str, String spliter){
		if(str == null)
			return 0;
		int lastPos = 0, v = 0;
		for(int i=0; i<str.length(); i++){
			int pos = str.indexOf(spliter, i);
			if(pos>lastPos+spliter.length()-1){
				v++;
				i = pos + spliter.length()-1;
			}
			if(pos<0) v++;
			lastPos = pos + spliter.length();
		}
		return v;
	}

	public static String getParentPath(String path, String spliter){
		if(path == null) return path;
		int p = path.lastIndexOf(spliter);
		while(p >= spliter.length()){
			if(path.regionMatches(p-spliter.length(), spliter, 0, spliter.length())){
				p -= spliter.length();
			}else break;
		}
		return p >= 0 ? path.substring(0, p) : path;
	}

	public static String trimPathToLevel(String str, String spliter, int level){
		if(str == null) return null;
		int lastPos = 0, v = 0;
		for(int i=0; i<str.length(); i++){
			int pos = str.indexOf(spliter, i);
			if(pos>lastPos+spliter.length()-1){
				v++;
				if(v==level) return str.substring(0,pos);
				i = pos + spliter.length()-1;
			}
			if(pos<0) v++;
			lastPos = pos + spliter.length();
		}
		return str;
	}
	
	/**
	 * 判断文字内容重复
	 * @author 沙枣
	 * @Date 2008-04-17
	 */
	public static boolean isContentRepeat(String content) {
		if(content == null || content.length() < 2) return false;
		int similarNum = 0;
		//确定测试片段的最大长度
		int xSegSize = 3000;
		int tContLen = (int) Math.ceil(content.length() / 1000);
		if(tContLen < 9) xSegSize = (1 + tContLen / 3) * tContLen * 100;
		if(xSegSize > content.length() / 2) xSegSize = content.length() / 2;
		
		//从片段长度1开始测试
		for (int j = 1; j < xSegSize; j++) {
			
			//确定该长度下的测试次数
			int forNum = (int) Math.floor(0.7 * content.length() / j);

			//循环对比相邻片段，相同则增加相似值，否则重置相似值
			for (int m = 0; m < forNum; m++) {
				if (content.regionMatches(m*j, content, (m+1)*j, j))
					if (1.0 * (++similarNum) / forNum > 0.4) return true;
				else
					similarNum = 0;
			}
		}
		return false;
	}

	/**
	 * 判断内容是否由一些重复的片段构成
	 * @param cont 内容文本
	 * @return 重复率达到阀值则返回 true
	 * @author Liuch
	 */
	public static boolean isRubbish(String cont){
		if(cont.length()<2) return false;
		float threshold = 0.6f; //阀值
		int thresholdPos = (int) Math.ceil(cont.length() * (1-threshold));
		//逐字循环，start位置为每次比较的起点
		for(int start = 0; start < thresholdPos; start++){
			char ch = cont.charAt(start);
			int pos0 = cont.indexOf(ch, start+1);
			if(pos0 < 0) continue;
			int maxLen = 1, minLen = 0, totalLen = 0, count = 1, gapLen = 0, lastPos = start;
			for(int pos = pos0; pos<cont.length() && pos>0; pos = cont.indexOf(ch, pos)){
				int off = 1;
				for(; pos+off<cont.length() && start+off<pos; off++){
					char c1 = cont.charAt(start+off);
					char c2 = cont.charAt(pos+off);
					if(c1 != c2){
						break;
					}
				}
				if(off > 1){
					if(off > maxLen) maxLen = off;
					totalLen += off;
				}
				if(minLen == 0 || off<minLen) minLen = off;
				gapLen += pos-lastPos-off;
				lastPos = pos;
				pos+=off;
				count++;
			}
			if(count>1){
				totalLen += maxLen;
				if(minLen == 0) minLen = 1;
//				打印关键信息 测试用
//				System.out.println("char:"+ch+" start:"+start+" min:"+minLen+" max:"+maxLen
//						+" total:"+totalLen+" count:"+count+" rateX:"+(1.0*totalLen/cont.length())+
//						" rateM:"+1.0*count*minLen/cont.length());
				double rate = 1.0*totalLen/cont.length();
				if(rate > threshold) return true;
				if(1.0*count*minLen/cont.length() > threshold) return true;
			}
		}
		return false;
	}
	
	//TODO===========================test============================
	public static String joinPair(Map<?,?> m, String joint, String sep){
		if(m == null || m.size()<=0) return "";
		StringBuilder sb = new StringBuilder();
		for(Entry<?, ?> pair : m.entrySet()){
			sb.append(pair.getKey())
			.append(joint)
			.append(pair.getValue())
			.append(sep);
		}
		sb.setLength(sb.length()-sep.length());
		return sb.toString();
	}
	protected static String joinPair(Map<?,?> m){
		return joinPair(m, " = ", " , ");
	}
	protected static void ln(Object o){
		System.out.println(String.valueOf(o));
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
//		String[] test = {
//			"111111222222333333444444555555666666",
//			"1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1",
//			"1a_11b_111c_1111d_11111e_111111f_111g1111_111h11111_111i111111_111j1111111_",
//			"he says get off, get off! that's enough. then he got an offer.",
//			"abcdefg_2014_1_1"
//		};
//		int repeat = 1;
//		for(int x = 0; x<test.length; x++){
//			String t = test[x];
//			ln(x+repeat("=",10));
//			Long s = System.currentTimeMillis();
//			for(int i = 0; i < repeat; i++){
//				isRubbish(t);
//			}
//			Long e = System.currentTimeMillis();
//			ln(isRubbish(t));
//			ln(e-s);
//			
//			s = System.currentTimeMillis();
//			for(int i = 0; i < repeat; i++){
//				isContentRepeat(t);
//			}
//			e = System.currentTimeMillis();
//			ln(isContentRepeat(t));
//			ln(e-s);
//		}
		
		String[] aa = {"11"};
		System.out.println(joinString(aa,",0,"));
	}	
	//===============================test end========================
	
	/**
	 * 保留小数点后两位<br/>
	 * TODO 使用 UnitConvertUtil.doubleRetainDecimal 或者移动到 NumericUtil
	 */
	public static double convertNumber(double d){
		if(d==0)return 0;
		if(((int)(d*100)/100D)>0){
			return ((int)(d*100)/100D);
		}else{
			if((d - ((int)(d*100)/100D))>0){
				return 0.01;
			}else{
				return 0;
			}
		}
	}

	/**
	 * 尝试获取字符串使用的编码
	 * TODO 验证该方法的可用性
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		for(String encode: supportedEncode){
			try{
				if(str.equals(new String(str.getBytes(encode), encode)))
					return encode;
			}catch(Exception e){}
		}
		return "";
	}
	private static String[] supportedEncode = "GB2312,ISO-8859-1,UTF-8,GBK".split(",");
	
}
