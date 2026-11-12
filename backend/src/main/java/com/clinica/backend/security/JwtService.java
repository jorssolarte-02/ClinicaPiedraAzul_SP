package com.clinica.backend.security;

import com.clinica.backend.entity.UserAccount;
import tools.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String secret; private final long expiration; private final ObjectMapper mapper;
    public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expiration,ObjectMapper mapper){this.secret=secret;this.expiration=expiration;this.mapper=mapper;}
    public String issue(UserAccount user){try{long now=Instant.now().getEpochSecond(); Map<String,Object> h=Map.of("alg","HS256","typ","JWT"); Map<String,Object> p=Map.of("sub",user.getId().toString(),"email",user.getEmail(),"role",user.getRole().name(),"iat",now,"exp",now+expiration/1000); String a=enc(mapper.writeValueAsBytes(h))+"."+enc(mapper.writeValueAsBytes(p)); return a+"."+enc(sign(a));}catch(Exception e){throw new IllegalStateException("No fue posible generar el token",e);}}
    public Map<String,Object> verify(String token){try{String[] parts=token.split("\\."); if(parts.length!=3||!MessageDigestSupport.equal(sign(parts[0]+"."+parts[1]),Base64.getUrlDecoder().decode(parts[2]))) return null; Map<String,Object> p=mapper.readValue(Base64.getUrlDecoder().decode(parts[1]),Map.class); Object exp=p.get("exp"); if(!(exp instanceof Number n)||n.longValue()<=Instant.now().getEpochSecond())return null; return p;}catch(Exception e){return null;}}
    private byte[] sign(String value)throws Exception{Mac mac=Mac.getInstance("HmacSHA256");mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));}
    private String enc(byte[] b){return Base64.getUrlEncoder().withoutPadding().encodeToString(b);}
    private static class MessageDigestSupport{static boolean equal(byte[] a,byte[] b){return java.security.MessageDigest.isEqual(a,b);}}
}
