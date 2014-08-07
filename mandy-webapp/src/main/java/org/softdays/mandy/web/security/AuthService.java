package org.softdays.mandy.web.security;

import org.softdays.mandy.dto.ResourceDto;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public ResourceDto getCurrentToken() {
	SecurityContext ctx = SecurityContextHolder.getContext();
	MyUser user = (MyUser) ctx.getAuthentication().getPrincipal();

	return user == null ? null : user.getResource();
    }

}
