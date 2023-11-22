package com.lucloud.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);


	/**
	 * 向cookie中写入一条记录
	 * @param name
	 * @param value void
	 * @author sam
	 * @since 2018年4月11日
	 */
	public static  void setCookieValue(HttpServletResponse response, String name, String value){
		try{
			Cookie cookie = new Cookie(name,value);
			cookie.setPath("/");
			response.addCookie(cookie);
		}catch(Exception e){
			logger.error("向cookie中写入name="+name+",value="+value+"出现异常：",e);
		}
	}
	/**
	 *
	 * @param name
	 * @param value
	 * @param expiry 失效时间（秒）
	 * @author sam
	 * @since 2018年5月15日
	 */
	public  static void  setCookieValue(HttpServletResponse response, String name, String value, int expiry){
		try{
			Cookie cookie = new Cookie(name,value);
			cookie.setMaxAge(expiry);
			cookie.setPath("/");
			response.addCookie(cookie);
		}catch(Exception e){
			logger.error("向cookie中写入name="+name+",value="+value+"出现异常：",e);
		}
	}
	/**
	 * 删除指定的cookie
	 * @param name void
	 * @author sam
	 * @since 2018年5月15日
	 */
	public  static void deleteCookie(HttpServletResponse response, String name){
		try{
			Cookie cookie = new Cookie(name,"");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}catch(Exception e){
			logger.error("删除cookie"+name+"出现异常：",e);
		}
	}



	public static String getCookieValue(HttpServletRequest req, String key){
		String value=null;
		try{
			Cookie[] cookies = req.getCookies();
			if(cookies == null)
				return null;
			//遍历方式查找Cookies中是否存在 name
			for(Cookie c : cookies){
				if(c.getName().equals(key)){//在cookies中找到name的cookie
					value = c.getValue();
					break;
				}
			}
		}catch(Exception e){
			logger.error("",e);
		}
		return value;
	}

	private static String getNonceCookieValue(){
		StringBuilder nonceStr=new StringBuilder();
		long currentTime= System.currentTimeMillis();
		//由当前时间和指定位数的随机字符串组成
		nonceStr.append(currentTime);
		nonceStr.append(NonceStrUtils.generateCommonStr(10));
		return nonceStr.toString();
	}

	public static String setGetCookieValue(HttpServletResponse resp, String key){
		String cookieValue=getNonceCookieValue();
		try{
			Cookie cookie = new Cookie(key,cookieValue);
			cookie.setPath("/");
			resp.addCookie(cookie);
		}catch(Exception e){
			cookieValue=null;
			logger.error("",e);
		}
		return cookieValue;
	}

	//删除Cookies
	public static void deleteCookies(HttpServletResponse resp , String key){
		Cookie newCookie=new Cookie(key,null); //假如要删除名称为username的Cookie
		newCookie.setMaxAge(0); //立即删除型
		newCookie.setPath("/"); //项目所有目录均有效，这句很关键，否则不敢保证删除
		resp.addCookie(newCookie); //重新写入，将覆盖之前的
	}
	
}
