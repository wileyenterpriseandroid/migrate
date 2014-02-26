package com.migrate.security;

import com.migrate.rest.DataController;
import com.migrate.security.UserCreator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    private static org.apache.log4j.Logger log = Logger.getLogger(DataController.class);

    @Autowired
    @Qualifier(value = "userDetailsManager")
    private JdbcUserDetailsManager userDetailsManager;

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
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Map<String, String> createObject(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse resp) throws IOException
    {
        System.out.println("username :" + username);

        UserCreator userCreator = new UserCreator();
        userCreator.setUsername(username);
        userCreator.setPassword(password);
        userCreator.setUserDetailsManager(userDetailsManager);
        userCreator.setGrantedAuthoritiesByName(Arrays.asList("ROLE_USER"));
        userCreator.setSaltSource(saltSource);
        userCreator.setPasswordEncoder(passwordEncoder);
        userCreator.setAccountNonExpired(true);
        userCreator.setAccountNonLocked(true);
        userCreator.setCredentialsNonExpired(true);
        userCreator.setEnabled(true);
        userCreator.createUser();

        return null;
    }
}
