package com.example.admin.common.service.gdcb.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	public static final String LF = System.getProperty("line.separator");
	
	
	/**
	 * 
	 * 문자열이 null 또는 빈문자열인지 체크한다.
	 * 
	 * @param str : 체크할 문자열
	 * @return : boolean
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 문자열 str을 문자 x로 구분하여 String[] 을 만들어 돌려준다
	 * 
	 * @param str
	 * @param x
	 */
	public static String[] split(String str, char x)
	{
		Vector v = new Vector();
		StringBuffer str1 = new StringBuffer();

		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == x)
			{
				v.add(str1);
				str1 = new StringBuffer();
			}
			else
			{
				str1.append(str.charAt(i));
			}
		}
		v.add(str1);
		String array[];
		array = new String[v.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = ((StringBuffer)v.elementAt(i)).toString().trim();
		}
		return array;
	}

	
	/**
	 * <pre>
	 * 문자열 str을 문자 x로 구분하여 String[] 을 만들어 돌려준다
	 * 첫번째 오는 x까지만 검사하고 그다음 오는 X는 무시 => 즉 length가 길어야 2
	 * ex)1111.22222.333333 => 1111   22222.333333   으로 분리
	 * </pre>
	 * @param str
	 * @param x
	 */
	public static String[] split2(String str, char x) 
	{ 
		java.util.ArrayList retVal = new java.util.ArrayList(); 
		int i = 0; 
		while(true) { 
			int end = str.indexOf(x, i); 
			if(end < 0) { 
				retVal.add(str.substring(i)); 
				break; 
			} else{
				retVal.add(str.substring(i, end)); 
				i = end + 1; 
				retVal.add(str.substring(i, str.length())); 
				break;
			}
		} 
		return (String[])(retVal.toArray(new String[0])); 
	}

	/**
	 * <pre>
	 * name/value => String( namelength + ':' + name + valuelength + ':' + value )
	 * </pre>
	 * @param name, value
	 * @return String
	 */
	public static String formatParam(String name, String value)
	{
		StringBuffer s = new StringBuffer();
		s.append(Integer.toString(name.getBytes().length));
		s.append(":");
		s.append(name);
		if (value == null)
		{
			s.append("0:");
		}
		else
		{
			s.append(Integer.toString(value.getBytes().length));
			s.append(":");
			s.append(value);
		}
		return s.toString();
	}
	
	/**
	 * <PRE>
	 * Comment : 랜덤 문자열을 생성한다.
	 *
	 * </PRE>
	 *   @return String
	 *   @param type 1:숫자 A:대문자 a:소문자 A1:대문자+숫자 a1:소문자+숫자 Z:대소문자+숫자 
	 *   @param len 생성할 문자열의 길이
	 *   @return 문자열
	 */
	public static String makeRndomString(String type, int len) {
		
		String pattern = "[a-zA-Z0-9]+$";

		if (type.equals("1")) pattern = "[0-9]+$";
		else if (type.equals("A")) pattern = "[A-Z]+$";
		else if (type.equals("a")) pattern = "[a-z]+$";
		else if (type.equals("A1")) pattern = "[A-Z0-9]+$";
		else if (type.equals("a1")) pattern = "[a-z0-9]+$";
		else pattern = "[a-zA-Z0-9]+$";
		
		Pattern p = Pattern.compile(pattern);
		
		StringBuffer sb = new StringBuffer();
		
        for(int i=0; i<Math.abs(len); i++) {
            getRndChar(sb, p);
        }

		return sb.toString();
	}
	
    //Get Random Character
    private static void getRndChar(StringBuffer sb, Pattern pattern) {
        int r = (int)Math.round(Math.random() * 1000);

        Random random = new Random();
        int rnd = random.nextInt(1000);
        
        Matcher m = pattern.matcher(String.valueOf((char)rnd));
        
        if(m.matches()) {
            sb.append((char)rnd);
        } else {
            getRndChar(sb, pattern);
        }
    }
    
	
	public static String getExceptionTrace(Exception ee) {
		String rtn = "";

		try {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);

			ee.printStackTrace(pw);

			rtn = sw.toString();

			pw.close();
			sw.close();

		} catch (IOException ioe) {
			rtn = "getExceptionTrace(Exception ee) : " + ioe.toString();
		}

		return rtn;
	}

	public static String getErrorTrace(Error err) {
		String rtn = "";

		try {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);

			err.printStackTrace(pw);

			rtn = sw.toString();

			pw.close();
			sw.close();

		} catch (IOException ioe) {
			rtn = "getExceptionTrace(Exception ee) : " + ioe.toString();
		}

		return rtn;
	}
	
	/**
	 * LPAD기능
	 * @param str
	 * @param c Padding할 캐릭터
	 * @param len
	 * @return
	 */
	public static String lpad(String str, char c, int len) {
		if (str == null) return null;
		if (str.length() >= len) return str;
		
		StringBuffer sb = new StringBuffer();
		int pLen = len - str.length();
		for (int i=0; i<pLen; i++) {
			sb.append(c);
		}
		sb.append(str);
		return sb.toString();
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : 문자열에서 숫자만 추출한다.(CTN 추출 등에 사용)
	 *
	 * </PRE>
	 *   @return String
	 *   @param data
	 *   @return
	 */
	public static String removeFormat(String data) {
		
		if (data==null) return "0";
		if (data.trim().equals("")) return "0";
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<data.length(); i++) {
			if (Character.isDigit(data.charAt(i))) {
				sb.append(data.charAt(i));
			}						
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : HTML 템플릿 파일에서 $[xxx] 포맷의 변수를 실제 값으로 치환한다.
	 *
	 * </PRE>
	 *   @return String 치환 후의 전체 파일 내용
	 *   @param f 템플릿 파일
	 *   @param map 변수명, 치환할 값
	 *   @param charset Character Set (Option), Null이면 UTF-8 사용
	 *   @return
	 */
	public static String mappingConvert(File f, HashMap<String, String> map, String charset) {
		
		String returnStr = null;
		BufferedReader br = null;
		
		try {
			
			if (! f.exists()) return null;
			
			StringBuffer sb = new StringBuffer();
			
			if (charset == null) charset = "UTF8";
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(LF);
			}
			br.close();
			br = null;
			
			returnStr = sb.toString();
			
			Set<String> set = map.keySet();
			Object[] keys = set.toArray();
			
			if (keys != null) {
				for (int i=0; i<keys.length; i++) {
					returnStr = returnStr.replace("$[" + keys[i].toString() + "]", map.get(keys[i]));
				}
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try { if(br != null) br.close(); }catch (Exception e) {}
		}
		
		return returnStr;
	}
	
	
	/**
	 * <PRE>
	 * Comment : 문자열을 Byte 단위로 자른다.
	 * 
	 * 한길 기본 2byte로 자른다.
	 * 
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @param endIndex
	 *   @param
	 *   @return
	 *   @throws Exception
	 */
	public static String cutStringByte(String str, int endIndex) throws Exception{
		return cutStringByte(str, endIndex, "");
	}
	
	/**
	 * <PRE>
	 * Comment : 문자열을 CharSet Byte 단위로 자른다.
	 * 
	 * 한글 UTF-8 은 3byte로 자른다.
	 * 
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @param endIndex
	 *   @param charSet
	 *   @return
	 *   @throws Exception
	 */
	public static String cutStringByte(String str, int endIndex, String charSet) throws Exception{
		StringBuffer sbStr = new StringBuffer(endIndex);
		int iTotal = 0;
		for (char c : str.toCharArray()) {
			if("".equals(charSet))
				iTotal += String.valueOf(c).getBytes().length;
			else
				iTotal += String.valueOf(c).getBytes(charSet).length;
			
			if (iTotal > endIndex) {
				break;
			}
			sbStr.append(c);
		}
		return sbStr.toString();
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : SNS Hub GW 연동시 응답코드를 "SN"로 시작하는 코드로 치환한다.
	 *
	 * </PRE>
	 *   @return String 변환된 XML
	 *   @param xml : SNS 응답 XML
	 *   @return
	 */
	public static String converSnsResultCode(String xml) {
		
		String retXML = xml;
		if (xml == null) return null;
		
		StringBuffer sb = new StringBuffer();
		
		try {
			
			int idx1 = xml.indexOf("<code>");
			int next = 0;
			
			while (idx1 > -1) {
				
				sb.append(xml.subSequence(next, idx1));
				
				int idx2 = xml.indexOf("</code>", next);
				
				if (idx2 > -1) {
					String code = xml.substring(idx1 + 6, idx2);
					if("200".equals(code)){ // 성공코드
						sb.append("<code>00000</code>");
					}else{ // 실패코드
						sb.append("<code>SN" + code.substring(0, 3) + "</code>");
					}
				}
				next = idx2 + 7;
				idx1 = xml.indexOf("<code>", next);
			}
			
			if (next > 0) {
				sb.append(xml.substring(next));
			}
			
			retXML = sb.toString();
			
		} catch (Exception ex) {
			return xml;
		}
		
		return retXML;
	}
	
	
	/**
	 * 
	 * <PRE>
	 * Comment : 숫자로만 이루어진 문자열인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @return
	 */
	public static boolean checkNumeric(String sData) {
		
		String sNumber = checkTrim(sData);
		if (sNumber.equals("") || (sNumber == null)) {
			return false;
		}
		
		for (int i=0; i<sData.length(); i++) {
			char ch = sData.charAt(i);
			if (!Character.isDigit(ch)) return false;
		}

		return true;
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : 숫자로만 이루어진 문자열인지 체크, (예외 문자 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param exceptStr
	 *   @return
	 */
	public static boolean checkNumeric(String sData, String exceptStr) {
		
		String str = checkTrim(sData);
		if (str.equals("") || (str == null) || exceptStr == null) {
			return false;
		}
		char[] arr = exceptStr.toCharArray();
		
		for (int i=0; i<sData.length(); i++) {
			char ch = sData.charAt(i);
			
			boolean chkFlag = false;
			
			for (int j=0; j<arr.length; j++) {
				if (ch == arr[j]) {
					chkFlag = true;
					break;
				}
			}
			if (chkFlag) continue;
			if (!Character.isDigit(ch)) return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * <PRE>
	 * Comment : 숫자로만 이루어진 문자열인지 체크 (길이 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param length
	 *   @return
	 */
	public static boolean checkNumeric(String sData, int length) {
		if (sData == null || sData.length() != length) return false;
		return checkNumeric(sData);
	}

	
	/**
	 * 
	 * <PRE>
	 * Comment : 영문+숫자로만 이루어진 문자열인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @return
	 */
	public static boolean checkAlphaNumeric(String sData) {
		
		String str = checkTrim(sData);
		if (str.equals("") || (str == null)) {
			return false;
		}
		
		for (int i=0; i<sData.length(); i++) {
			char ch = sData.charAt(i);
			if (!Character.isLetterOrDigit(ch)) return false;
			else if (ch >= '가' && ch <= '힣')  return false;
		}
		return true;
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : 영문+숫자로만 이루어진 문자열인지 체크 (길이 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param length
	 *   @return
	 */
	public static boolean checkAlphaNumeric(String sData, int length) {
		if (sData == null || sData.length() != length) return false;
		return checkAlphaNumeric(sData);
	}

	
	/**
	 * 
	 * <PRE>
	 * Comment : 영문+숫자로만 이루어진 문자열인지 체크, (예외 문자 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param exceptChar 예외문자
	 *   @return
	 */
	public static boolean checkAlphaNumeric(String sData, char exceptChar) {
		
		String str = checkTrim(sData);
		if (str.equals("") || (str == null)) {
			return false;
		}
		
		for (int i=0; i<sData.length(); i++) {
			char ch = sData.charAt(i);
			if (ch == exceptChar) continue;
			if (!Character.isLetterOrDigit(ch)) return false;
			else if (ch >= '가' && ch <= '힣')  return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * <PRE>
	 * Comment : 영문+숫자로만 이루어진 문자열인지 체크, (예외 문자 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param exceptStr 예외문자열 ex: "_&.;#"
	 *   @return
	 */
	public static boolean checkAlphaNumeric(String sData, String exceptStr) {
		
		String str = checkTrim(sData);
		if (str.equals("") || (str == null) || exceptStr == null) {
			return false;
		}
		
		char[] arr = exceptStr.toCharArray();
		
		for (int i=0; i<sData.length(); i++) {
			char ch = sData.charAt(i);
			
			boolean chkFlag = false;
			
			for (int j=0; j<arr.length; j++) {
				if (ch == arr[j]) {
					chkFlag = true;
					break;
				}
			}
			if (chkFlag) continue;
			
			if (!Character.isLetterOrDigit(ch)) return false;
			else if (ch >= '가' && ch <= '힣')  return false;
		}
		return true;
	}

	
	/**
	 * 
	 * <PRE>
	 * Comment : 영문+숫자로만 이루어진 문자열인지 체크, (예외 문자 및 길이 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param exceptChar
	 *   @param length
	 *   @return
	 */
	public static boolean checkAlphaNumeric(String sData, char exceptChar, int length) {
		if (sData == null || sData.length() != length) return false;
		return checkAlphaNumeric(sData, exceptChar);
	}
	
	
	/**
	 * 
	 * <PRE>
	 * Comment : 범용문자로만 이루어진 문자열인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @return
	 */
	public static boolean checkLetter(String sData) {
		
		String str = checkTrim(sData);
		if (str.equals("") || (str == null)) {
			return false;
		}
		
		for (int i=0; i<sData.length(); i++) {
			char ch = sData.charAt(i);
			if (!Character.isLetterOrDigit(ch)) return false;
		}
		return true;
		
	}

	/**
	 * 
	 * <PRE>
	 * Comment : 범용문자로만 이루어진 문자열인지 체크 (길이 지정)
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param sData
	 *   @param length
	 *   @return
	 */
	public static boolean checkLetter(String sData, int length) {
		if (sData == null || sData.length() != length) return false;
		return checkLetter(sData);
	}
	
	
	/**
	 * 
	 * <PRE>
	 * Comment : 문자열 좌우 공백 제거. Null인 경우 "" 리턴
	 *
	 * </PRE>
	 *   @return String
	 *   @param sData
	 *   @return
	 */
	public static String checkTrim(String sData) {
		if (sData == null){
			return "";
		} 
		return sData.trim();
	}

		 

	/**
	 * 
	 * <PRE> 유효한 날짜인지 체크
	 * Comment : 
	 *          예) checkValidDate("20110101")
	 * </PRE>
	 *   @return boolean
	 *   @param sDate
	 *   @return
	 */
	public static boolean checkValidDate(String sDate) {
		
		int iYear = 0;
		int iMonth = 0;
		int iDay = 0;
		int iLastday = 0;

		int days[]={0,31,28,31,30,31,30,31,31,30,31,30,31};

		sDate = checkTrim(sDate);

		if (sDate.length() != 8) {
			return false;
		}
		if (!checkNumeric(sDate)) {
			return false;
		}
		iYear  = Integer.valueOf(sDate.substring(0,4)).intValue();
		iMonth = Integer.valueOf(sDate.substring(4,6)).intValue();
		iDay   = Integer.valueOf(sDate.substring(6,8)).intValue();

		if ( iYear < 1900) {
			return false;
		}

		if ( iMonth < 1 || iMonth > 12){ 
			return false;
		}

		if (iMonth == 2)  {
			if((iYear%4 == 0) && (iYear%100!=0) || (iYear%400 ==0))
				iLastday = 29;
			else
				iLastday = 28;
		} else {
			iLastday = days[iMonth];
		}

		if ( iDay < 1 || iDay > iLastday) {
			return false;
		}
		return true;
	}



	/**
	 * 
	 * <PRE>
	 * Comment :문자열(origin_txt) 안에 있는 일부문자열(find_str)을 원하는 문자열(replace_str)로 바꿔준다
	 *
	 * </PRE>
	 *   @return String
	 *   @param origin_txt 원본 문자열
	 *   @param find_str 바꾸고 싶은 문자열
	 *   @param replace_str 바꿀 문자열
	 *   @return 결과 String
	 */
	public static String replaceInStr(String origin_txt, String find_str, String replace_str) {
		int itmp = 0;
		if (origin_txt==null) return "";

		String tmp = origin_txt;
		StringBuffer sb = new StringBuffer();
		sb.append("");
		while (tmp.indexOf(find_str)>-1) {
			itmp = tmp.indexOf(find_str);
			sb.append(tmp.substring(0,itmp));
			sb.append(replace_str);
			tmp = tmp.substring(itmp+find_str.length());
		}
		sb.append(tmp);
		return sb.toString();
	}

	/**
	 * 
	 * <PRE>
	 * Comment : 이메일이 유효한 형식인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param email
	 *   @return
	 */
	public static boolean isValidEmail(String email) {
		//Pattern p = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$");
		Pattern p = Pattern.compile("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}");
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : URL이 유효한 형식인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param url
	 *   @return
	 */
	public static boolean isValidURL(String url) {
		if (url == null || url.trim().equals("")) return false;
		if (!url.endsWith("/")) url = url + "/";
		Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
		Matcher mc = urlPattern.matcher(url);
		return mc.matches(); 
	}
	
	
	/**
	 * 
	 * <PRE>
	 * Comment : 070번호가 유효한 형식인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param voip_tel_no
	 *   @return
	 */
	public static boolean isValidVoipNum(String voip_tel_no) {
		String str = checkTrim(voip_tel_no);
		if (str.equals("") || (str == null)) {
			return false;
		}
		return Pattern.matches("(\\d{3})-(\\d{4})-(\\d{4})", voip_tel_no);
	}
	
	/**
	 * 
	 * <PRE>
	 * Comment : ctn번호가 유효한 형식인지 체크
	 *
	 * </PRE>
	 *   @return boolean
	 *   @param
	 *   @return
	 */
	public static boolean isValidCtnNum(String ctn_no) {
		String str = checkTrim(ctn_no);
		if (str.equals("") || (str == null)) {
			return false;
		}
		return Pattern.matches("(\\d{3})-(\\d{4})-(\\d{4})", ctn_no);
	}
	
	/**
	 * <PRE>
	 * Comment : 휴대전화 형식으로 가져온다.
	 * </PRE>
	 *   @return String
	 *   @param ctn
	 *   @return
	 */
	public static String getCtnFormat(String ctn){
		int size = ctn.length();
		
		StringBuffer sb = new StringBuffer(ctn);
		
		if(size == 10){
			sb.insert(3, "-");
			sb.insert(7, "-");
		}else if(size == 11){
			sb.insert(3, "-");
			sb.insert(8, "-");
		}
		
		return sb.toString();
	}
	
	
	/**
	 * <PRE>
	 * Comment : 주소형태 형식으로 가져온다.
	 * </PRE>
	 *   @return String
	 *   @param zipcode
	 *   @return
	 */
	public static String getZipCodeFormat(String zipcode){
		int size = zipcode.length();
		
		StringBuffer sb = new StringBuffer(zipcode);
		
		if(size == 6){
			sb.insert(3, "-");
		}
		
		return sb.toString();
	}

	//몇개월전 MONTH 구하기
	public static String getMonth(int before){
		Calendar cal = Calendar.getInstance();
		cal.add ( cal.MONTH, -before );
		int mo =  cal.get ( cal.MONTH ) +1;
		String month;
		if(mo<10){
			month = "0"+String.valueOf(mo);
		}else{
			month = String.valueOf(mo);
		}
		return month;
	}
	
	//year 구하기
	public static String getYear(int before){
		Calendar cal = Calendar.getInstance ( );
		cal.add ( cal.YEAR, -before );
		int mo =  cal.get(cal.YEAR )%5;
		String year;
		if(mo==0){
			year = "05";
		}else{
			year = "0"+String.valueOf(mo);
		}
		return year;
	}
	
	//문자열을 특정 길리로 잘라 변환 한다.
	public static String srtReplace(String oldChar, int beginIndex, int endIndex){
		String newChar = "";
		String replaceChar = "";
		int subCharCnt = 0;
		
		if(oldChar != null){
			String subChar = oldChar.substring(beginIndex, endIndex);
			subCharCnt = subChar.length();
			for(int i=0; i<subCharCnt; i++){
				replaceChar = replaceChar+"*";
			}
			newChar = oldChar.replaceAll(subChar, replaceChar);
		}
		return newChar;
	}
	
	//문자열을 특정 길리로 잘라 변환 한다.
	public static String srtReplaceTel(String oldChar){
		String subChar = "";
		
		String no1 = "";
		String no2 = "";
		String no3 = "";
		
		int strLength = 0;
				
		if(oldChar != null){
			strLength = oldChar.length();
			
			if(strLength == 10){
				//010 111 2222
				no1 = oldChar.substring(0, 3);
				no2 = "***";
				no3 = oldChar.substring(6, strLength);
			}else{
				no1 = oldChar.substring(0, 3);
				no2 = "****";
				no3 = oldChar.substring(7, strLength);
			}
		}
		
		return no1+no2+no3;
	}
	
	//문자열을 특정 길리로 잘라 변환 한다.
	public static String srtReplaceEm(String oldChar){
		String subChar = "";
		String value = "";
		
				
		if(oldChar != null){
			subChar = oldChar.substring(oldChar.indexOf("@")-4, oldChar.indexOf("@")+1);
			value = oldChar.replace(subChar, "****@");
			
		}
		
		return value;
	}

	/**yyyyMMddHHmmss 구하기*/
	public static String getFormat() {
		String key = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		return key;
	}
	
	/**
	 * 현재 날짜 기준으로 원하는 시점의 날짜를 구함.
	 * @param field : Calendar.DATE - 일, Calendar.WEEK_OF_MONTH - 주, Calendar.MONTH - 월, Calendar.YEAR - 년
	 * @param amount : 현재 날짜 기준으로 구하고자 하는 년,월,일,주 - 이후는 양수 이전은 음수(10, -10)
	 * @param format : 날짜 포멧
	 * @return String : 포멧 형태의 날짜
	 */
	public static String addDateFromNow(int field, int amount, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(field, amount);
			  
		Date date = calendar.getTime();
			  
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
//	/**
//	 * <PRE>
//	 * Comment :
//	 *
//	 * </PRE>
//	 *   @return void
//	 *   @param args
//	 */
//	public static void main(String[] args) {
//		
//		try {
//			
//			System.out.println("---------- 이메일 ---------");
//			
//			String str = "kbk-123@chol.com";
//			System.out.println(str + ":\t" + isValidEmail(str) );
//			str = "6025@ez-i.co.kr";
//			System.out.println(str + ":\t" + isValidEmail(str) );
//			str = "aa#a@gmail.com";
//			System.out.println(str + ":\t" + isValidEmail(str) );
//			str = " ";
//			System.out.println(str + ":\t" + isValidEmail(str) );
//			str = "te&sta@lguplus.co.kr";
//			System.out.println(str + ":\t" + isValidEmail(str) );
//			
//			System.out.println("--------- 영문+숫자 ----------");
//			
//			str = "123abc";
//			System.out.println(str + ":\t" + checkAlphaNumeric(str)); // true
//			str = "김123abc";
//			System.out.println(str + ":\t" + checkAlphaNumeric(str));  //한글 false
//			str = "123 abc";
//			System.out.println(str + ":\t" + checkAlphaNumeric(str, '.')); //false
//			
//			System.out.println("---------- 범용 문자 ---------");
//			
//			str = "김123abc";
//			System.out.println(str + ":\t" + checkLetter(str));
//			str = "abc#123";
//			System.out.println(str + ":\t" + checkLetter(str));
//			str = "abc&123";
//			System.out.println(str + ":\t" + checkLetter(str));
//			str = "abc>123";
//			System.out.println(str + ":\t" + checkLetter(str));
//			str = "김123 abc";
//			System.out.println(str + ":\t" + checkLetter(str));
//			
//			System.out.println("--------- 영문+숫자+예외문자 ----------");
//			
//			str = "abc&123_aaa.999";
//			System.out.println(str + ":\t" + checkAlphaNumeric(str, "_.&"));
//			
//			System.out.println("--------- 070번호 ----------");
//
//			str = "007012341234";
//			System.out.println(str + ":\t" + isValidVoipNum(str));
//			str = "07012341234";
//			System.out.println(str + ":\t" + isValidVoipNum(str));
//			str = "0070-1234-1234";
//			System.out.println(str + ":\t" + isValidVoipNum(str));
//			str = "070-1234-1234";
//			System.out.println(str + ":\t" + isValidVoipNum(str));
//			
//			System.out.println("---------- URL ---------");
//
//			str = "https://www.ez-i.co.kr";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://localserver/projects/public/assets/javascript/widgets/UserBoxMenu/widget.css";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://a.co.kr/";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://www.google.co.uk/projects/my%20folder/test.php";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "https://myserver.localdomain";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://192.168.1.120/projects/index.php";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://192.168.1.1/projects/index.php";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://projectpier-server.localdomain/projects/public/assets/javascript/widgets/UserBoxMenu/widget.css";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "https://2.4.168.19/project-pier?c=test&a=b";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "https://localhost/a/b/c/test.php?c=controller&arg1=20&arg2=20";
//			System.out.println(str + ":\t" + isValidURL(str));
//			str = "http://user:password@localhost/a/b/c/test.php?c=controller&arg1=20&arg2=20";
//			System.out.println(str + ":\t" + isValidURL(str));
//			
//		} catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
}



