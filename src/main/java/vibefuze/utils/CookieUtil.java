package vibefuze.utils;


import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void set(HttpServletResponse hsr, String name, String value, Boolean secure, Integer expire, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expire);
        cookie.setDomain(domain);
        cookie.setPath("/");
        hsr.addCookie(cookie);
    }

    public static void remove(HttpServletResponse hsr, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        cookie.setDomain("localhost");
        hsr.addCookie(cookie);
    }

    private static Cookie get(HttpServletRequest request, String name){
        return WebUtils.getCookie(request, name);
    }
}
