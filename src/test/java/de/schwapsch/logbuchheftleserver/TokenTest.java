package de.schwapsch.logbuchheftleserver;

import de.schwapsch.logbuchheftleserver.auth.ServerAuthConfig;
import de.schwapsch.logbuchheftleserver.service.TokenService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ResourceBundle;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({ServerAuthConfig.class, TokenService.class})
public class TokenTest {
    @Autowired
    MockMvc mvc;

    private final ResourceBundle resource = ResourceBundle.getBundle("credentials");
    private final String serverAuthPassword = resource.getString("cred.serverPw") ;
    private final String serverAuthUsername = resource.getString("cred.serverUser");

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        // @formatter:off
        MvcResult result = this.mvc.perform(post("/auth")
                        .with(httpBasic(serverAuthUsername, serverAuthPassword)))
                .andExpect(status().isOk()).andReturn();

        String token = result.getResponse().getContentAsString();

        this.mvc.perform(get("/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Logged in!"));
        // @formatter:on
    }

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        // @formatter:off
        this.mvc.perform(get("/auth"))
                .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    void tokenWhenBadCredentialsThen401() throws Exception {
        // @formatter:off
        this.mvc.perform(post("/auth"))
                .andExpect(status().isUnauthorized());
        // @formatter:on
    }

}
