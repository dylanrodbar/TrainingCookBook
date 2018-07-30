package initial.security;


import static initial.constants.SecurityConstants.EXPIRATION_TIME;
import static initial.constants.SecurityConstants.SECRET;
import static initial.constants.SecurityConstants.HEADER_STRING;
import static initial.constants.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import initial.models.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    public JWTAuthenticationFilter() {
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                             HttpServletResponse res) throws AuthenticationException {
    	
    	
    	
    	try {
            Users creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Users.class);
            
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    	
		
    }
    
   
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

    	
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
                
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.setStatus(HttpServletResponse.SC_OK);
        
        
    }
    
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            AuthenticationException failed
                                            ) throws IOException, ServletException {

    	System.out.println(failed.toString());
    	res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	
    	
    }
    

}
