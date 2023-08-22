package com.example.chestGameServer.configs.MVC;

import com.example.chestGameServer.Controllers.UserController;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Enums.HttpAttributes;
import com.example.chestGameServer.Models.User.UserRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

public class HttpAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri=request.getRequestURL().toString();
        String path=ProtectedPaths.protectedPaths.keySet().stream()
            .filter(k->requestUri.contains(k))
            .findAny().get();
    if(path==null)return true;
        Set<UserRoles> requiredRoles=ProtectedPaths.protectedPaths.get(path);
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession httpSession = httpServletRequest.getSession(false);
        UserPrincipal userPrincipal= (UserPrincipal) httpSession.getAttribute(HttpAttributes.USER_PRINCIPAL.getName());
        if(userPrincipal==null){
            response.setStatus(401);
            response.sendRedirect(UserController.SIGN_IN);
            return false;
        }
        if(!requiredRoles.stream().anyMatch(role-> !userPrincipal.getUserRoles().contains(role))){
            response.setStatus(403);
            return false;
        }
    return true;
    }
}
