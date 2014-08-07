package org.softdays.mandy.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Responsabilité : récupérer le rôle utilisateur en base.
 * 
 * @author rpatriarche
 * 
 */
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username)
	    throws UsernameNotFoundException {
	return null;
    }

}
