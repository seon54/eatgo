package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.EmailNotExistedException;
import kr.co.fastcampus.eatgo.application.PasswordWrongException;
import kr.co.fastcampus.eatgo.application.UserService;
import kr.co.fastcampus.eatgo.domain.User;
import kr.co.fastcampus.eatgo.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SessionController.class)
class SessionControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @Test
    public void createWithValidAttributes() throws Exception {
        Long id = 1004L;
        String name = "Test";
        String email = "test@example.com";
        String password = "test";

        User mockUser = User.builder().id(id).name(name).build();

        given(userService.authenticate(email, password)).willReturn(mockUser);

        given(jwtUtil.createToken(id, name)).willReturn("header.payload.signature");

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"name\": \"Test\", \"password\": \"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/session"))
                .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")));

        verify(userService).authenticate(eq(email), eq(password));
    }

    @Test
    public void createWithNotExistedEmail() throws Exception {
        given(userService.authenticate("x@example.com", "test"))
                .willThrow(EmailNotExistedException.class);

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"x@example.com\", \"name\": \"Test\", \"password\": \"test\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("x@example.com"), eq("test"));
    }

    @Test
    public void createWithWrongPassword() throws Exception {
        given(userService.authenticate("test@example.com", "x"))
                .willThrow(PasswordWrongException.class);

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"name\": \"Test\", \"password\": \"x\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("test@example.com"), eq("x"));
    }
}