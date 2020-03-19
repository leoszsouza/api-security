package br.com.leo.forum.controller;

import br.com.leo.forum.config.security.TokenService;
import br.com.leo.forum.controller.dto.TokenDto;
import br.com.leo.forum.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form){
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        try{
            Authentication authenticate = authenticationManager.authenticate(dadosLogin);

            String token = tokenService.gerarToekn(authenticate);

            System.out.println(token);

            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
}
