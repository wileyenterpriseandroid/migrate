package com.migrate.security;

import com.migrate.rest.DataController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String DEFAULT_ROLES = "ROLE_USER";

    private static org.apache.log4j.Logger log = Logger.getLogger(DataController.class);

    @Autowired
    @Qualifier(value = "userDetailsManager")
    private JdbcUserDetailsManager userDetailsManager;

    @Autowired
    @Qualifier(value = "daoAuthenticationProvider")
    private org.springframework.security.authentication.dao.DaoAuthenticationProvider authProvider;

    @Autowired
    @Qualifier(value = "saltSource")
    private SaltSource saltSource;

    @Autowired
    @Qualifier(value = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    /*
     * create the JSON object with the given type and id.
     * TODO: needs https
     */
    @RequestMapping(value = "web", method = RequestMethod.POST)
    @ResponseBody
    public boolean newUserWeb(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletRequest request) throws IOException
    {
        try {
            createUser(username, password);
            sessionLogin(username, password, request);
            return true;
        } catch (Exception e) {
            log.error("Could not create user.", e);
            e.printStackTrace();
            return false;
        }
    }

    /*
     * create the JSON object with the given type and id.
     * TODO: needs https
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public boolean newUser(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password) throws IOException
    {
        try {
            createUser(username, password);

            return true;

        } catch (Exception e) {
            log.error("Could not create user.", e);
            e.printStackTrace();
            return false;
        }
    }

    private void createUser(String username, String password) {
        List<String> roleList = Arrays.asList(DEFAULT_ROLES);

        UserCreator userCreator = new UserCreator();
        userCreator.setUsername(username);
        userCreator.setPassword(password);
        userCreator.setUserDetailsManager(userDetailsManager);
        userCreator.setGrantedAuthoritiesByName(roleList);
        userCreator.setSaltSource(saltSource);
        userCreator.setPasswordEncoder(passwordEncoder);
        userCreator.setAccountNonExpired(true);
        userCreator.setAccountNonLocked(true);
        userCreator.setCredentialsNonExpired(true);
        userCreator.setEnabled(true);
        userCreator.createUser();
    }

    private void sessionLogin(String username, String password, HttpServletRequest request) {
        try {
            // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(username, password);
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = authProvider.authenticate(token);
            log.debug("Logging in with new user:" +  authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            log.error("Failure in autoLogin", e);
        }
    }

    public static final String ROLE_USER = "ROLE_USER";

    /*
     * Verify parameter credentials are valid.
     */
    @RequestMapping(value = "{username}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkCredentials(
            @PathVariable String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse resp) throws IOException
    {
        try {
            ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(ROLE_USER));

            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
            String storePassword = userDetails.getPassword();

            User user = new User(username, password, authorities);
            Object salt = saltSource.getSalt(user);
            boolean isValid = passwordEncoder.isPasswordValid(storePassword, password, salt);

            // TODO: why is the provider throwing BadCredentialsException?
//            Authentication auth = new UsernamePasswordAuthenticationToken(user, authorities);
//            isValid = authProvider.authenticate(auth);

            return isValid;
        } catch (Exception e) {
            log.error("Could not create user.", e);
            e.printStackTrace();
            return false;
        }
    }
}
