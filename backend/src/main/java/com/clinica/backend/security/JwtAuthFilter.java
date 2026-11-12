package com.clinica.backend.security;

import jakarta.servlet.*; import jakarta.servlet.http.*; import java.io.IOException; import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 private final JwtService jwt; public JwtAuthFilter(JwtService jwt){this.jwt=jwt;}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{String h=request.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer ")){Map<String,Object> claims=jwt.verify(h.substring(7));if(claims!=null){String role=String.valueOf(claims.get("role"));var auth=new UsernamePasswordAuthenticationToken(String.valueOf(claims.get("sub")),null,java.util.List.of(new SimpleGrantedAuthority("ROLE_"+role)));SecurityContextHolder.getContext().setAuthentication(auth);}}chain.doFilter(request,response);}
}
