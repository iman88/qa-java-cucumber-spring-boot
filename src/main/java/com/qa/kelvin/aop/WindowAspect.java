package com.qa.kelvin.aop;

import com.qa.kelvin.annotation.Window;
import com.qa.kelvin.service.WindowSwitchService;
import com.qa.page.Base;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WindowAspect {

    @Autowired
    private WindowSwitchService switchService;

    @Before("@target(window) && within(com.qa..*)")
    public void before(JoinPoint jp, Window window) {
        ((Base) jp.getTarget()).setPage(switchService.getPageByTitle(window.value()));
    }

    @After("@target(window) && within(com.qa..*)")
    public void after(JoinPoint jp, Window window) {
        Base base = (Base) jp.getTarget();
        base.setPage(base.defaultPage());
    }
}
