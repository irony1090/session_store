package prv.irony.java_api.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SpringUtils {


  public static Cookie getCookie(HttpServletRequest req, String key){
    Cookie[] cookies = req.getCookies();

    if(cookies == null) return null;

    for(int i=0; i<cookies.length; i++){
      Cookie cookie = cookies[i];
      if(cookie.getName().equals(key))
        return cookie;
    }
    return null;
  }

  
}
