package com.recceasy.idp.layers.domain;

import com.recceasy.idp.handlers.ExceptionHandlers.DomainAlreadyExistsException;
import com.recceasy.idp.utils.annotations.RequiresAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/domain")
@RestController
public class DomainController {

    @Autowired
    private DomainService domainService;

//    @PostMapping("/register")
//    @RequiresAuth
//    public Domain register(@RequestBody RegisterDomain registerDomain) throws DomainAlreadyExistsException {
//        return domainService.createDomain(new Domain(new CurrentUser().getTenantId(), registerDomain.getDomainName()));
//    }

    @PostMapping("/validate")
    @RequiresAuth
    public boolean domainExists(@RequestBody String domainName) throws DomainAlreadyExistsException {
        return domainService.domainExists(domainName);
    }


}
