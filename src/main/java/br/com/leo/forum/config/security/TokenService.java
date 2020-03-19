package br.com.leo.forum.config.security;

import br.com.leo.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToekn(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Date hoje =  new Date();
        Date dataExpiracao =  new Date(hoje.getTime()+ Long.valueOf(expiration));

        return Jwts.builder()
                .setIssuer("API Test Leo security")
                .setSubject(usuario.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {

        try{
            Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token);
            return  true;
        }catch (Exception e){
            return false;
        }
    }

    public Long getIdUsuario(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }
}
