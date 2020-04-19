package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.ReviewService;
import kr.co.fastcampus.eatgo.application.UserService;
import kr.co.fastcampus.eatgo.domain.Review;
import kr.co.fastcampus.eatgo.domain.User;
import kr.co.fastcampus.eatgo.interfaces.ReviewController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SessionController.class)
class SessionControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void create() throws Exception {
        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"name\": \"Test\", \"password\": \"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/session"))
                .andExpect(content().string("{\"accessToken\":\"ACCESSTOKEN\"}"));

        verify(userService).authenticate(eq("test@example.com"), eq("test"));
    }
}