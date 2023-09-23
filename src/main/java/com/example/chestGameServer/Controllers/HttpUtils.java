package com.example.chestGameServer.Controllers;

import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Enums.HttpAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class HttpUtils {
    public HttpSession getSession(boolean createNew) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    public HttpSession setUserPrincipal(UserPrincipal userPrincipal, boolean createNew) {
        HttpSession httpSession = getSession(createNew);
        httpSession.setAttribute(HttpAttributes.USER_PRINCIPAL.getName(), userPrincipal);
        return httpSession;
    }

    public HttpServletResponse setUserPrincipalCookie(HttpServletResponse httpResponse, String name, String pass) {
        Cookie userNameCookie = new Cookie(HttpAttributes.USER_NAME.getName(), name);
        Cookie userPassCookie = new Cookie(HttpAttributes.USER_PASS.getName(), pass);

        userNameCookie.setMaxAge(60 * 60);
        userNameCookie.setMaxAge(60 * 60);

        httpResponse.addCookie(userNameCookie);
        httpResponse.addCookie(userPassCookie);
        return httpResponse;
    }
}
