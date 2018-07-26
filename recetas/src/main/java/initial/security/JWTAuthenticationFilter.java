package initial.security;


import static initial.constants.SecurityConstants.EXPIRATION_TIME;
import static initial.constants.SecurityConstants.SECRET;

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

import initial.models.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/* 
   * con esa clase se van a procesar las credenciales de usuario, las cuales seran pasadas al AuthenticationManager
   * si las credenciales son correctas, entonces se va a generar un jwt para el usuario
*/
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
    	
    	String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        new ArrayList<>())
        );
		
    }
    
    public HashMap<String, String> returnJson(String jsonmsg) {
    	HashMap<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        return map;
    }

    
    /*
       * se llega a este metodo, en caso de que las credenciales del usuario sean correctas 
    */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

    	/*
    	   * aqui se genera el token 
    	*/
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        //res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        
        System.out.println("token");
        System.out.println(token);
        
        returnJson(token);
    }
    
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            AuthenticationException failed
                                            ) throws IOException, ServletException {

    	System.out.println(failed.toString());
    }
    

}
