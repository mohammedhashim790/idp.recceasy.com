package com.recceasy.idp.utils.annotations;

import com.recceasy.idp.layers.security.CurrentUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {

    @Before("@within(RequiresAuth) || @annotation(RequiresAuth)")
    public void checkAuth(JoinPoint joinPoint) {
        if (!CurrentUser.signedIn()) {
            throw new SecurityException("Unauthorized: User is not signed in.");
        }
    }
}
