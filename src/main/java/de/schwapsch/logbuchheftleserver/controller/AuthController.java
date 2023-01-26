package de.schwapsch.logbuchheftleserver.controller;

import de.schwapsch.logbuchheftleserver.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    public String token(Authentication authentication) {
        logger.info("auth() called");
        return tokenService.generateToken(authentication);
    }
}
