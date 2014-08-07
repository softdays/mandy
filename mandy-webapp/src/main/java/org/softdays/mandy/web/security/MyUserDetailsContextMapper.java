package org.softdays.mandy.web.security;

import java.util.Arrays;
import java.util.Collection;

import org.softdays.mandy.config.Configuration;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

/**
 * Responsabilité : récupérer les données utilisateur en base.
 * 
 * @author rpatriarche
 * 
 */
@Component
public class MyUserDetailsContextMapper implements UserDetailsContextMapper {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private Configuration configurationDto;

    // public MyUserDetailsContextMapper(ResourceService resourceService,
    // Configuration configurationDto) {
    // super();
    // this.resourceService = resourceService;
    // this.configurationDto = configurationDto;
    // }

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx,
	    String username, Collection<? extends GrantedAuthority> authorities) {
	ResourceDto resource = resourceService.findByUid(username);
	if (resource == null) {
	    String lastname = (String) ctx.getObjectAttribute(configurationDto
		    .getLdapAttributeForLastname());
	    String firstname = (String) ctx.getObjectAttribute(configurationDto
		    .getLdapAttributeForFirstname());
	    resource = resourceService.create(username, lastname, firstname);
	}
	String role = resource.getRole();
	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

	return new MyUser(resource, Arrays.asList(authority));
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
	// not used
    }

}
