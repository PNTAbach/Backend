package com.backend.pnta.Controllers;

import com.backend.pnta.Models.UserVenueLike.UserVenueLikeCreateDTO;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeDTO;
import com.backend.pnta.Services.UserVenueLike.UserVenueLikeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserVenueLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserVenueLikeService userVenueLikeService;

    private final String token = "Bearer mock-token";

    @Test
    void addLikeToVenue_shouldReturnCreated() throws Exception {
        UserVenueLikeCreateDTO dto = new UserVenueLikeCreateDTO();

        doNothing().when(userVenueLikeService).addLikeToVenue(any(UserVenueLikeCreateDTO.class), eq("mock-token"));

        mockMvc.perform(post("/user-venue-like/like")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Like added successfully"));
    }

    @Test
    void removeLikeFromVenue_shouldReturnOk() throws Exception {
        doNothing().when(userVenueLikeService).removeLikeFromVenue(1L, "mock-token");

        mockMvc.perform(delete("/user-venue-like/like/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().string("Like removed successfully"));
    }

    @Test
    void getLikesFromVenue_shouldReturnOk() throws Exception {
        List<UserVenueLikeDTO> likes = List.of(new UserVenueLikeDTO());

        Mockito.when(userVenueLikeService.getLikesFromVenue(1L)).thenReturn(likes);

        mockMvc.perform(get("/user-venue-like/likes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getNumberOfLikesFromVenue_shouldReturnOk() throws Exception {
        Mockito.when(userVenueLikeService.getNumberOfLikesFromVenue(1L)).thenReturn(5);

        mockMvc.perform(get("/user-venue-like/likes/count/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
