package com.kt.giga.home.b2b.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * com.kt.giga.home.b2b.util
 *      CookieUtils
 *
 * Cookie Utils Class
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-10 오후 4:32
 */
public class CookieUtils {

    public static void setCookie(HttpServletResponse response, String name, String value, String domain, int maxAge) {
        if (value == null) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (domain != null && !"".equals(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        Cookie cookie = null;
        for (Cookie cooky : cookies) {
            if (cooky.getName().equals(name)) {
                cookie = cooky;
                break;
            }
        }
        return cookie;
    }
}
