package org.softdays.mandy.web.security;

import java.util.Collection;

import org.softdays.mandy.dto.ResourceDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MyUser extends User {

    private static final long serialVersionUID = 1L;

    private ResourceDto resource;

    public MyUser(ResourceDto resource,
	    Collection<? extends GrantedAuthority> authorities) {
	super(resource.getLogin(), "N/A", authorities);
	this.resource = resource;
    }

    public ResourceDto getResource() {
	return resource;
    }

}
