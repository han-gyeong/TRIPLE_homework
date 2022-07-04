package com.triple.travelermileage.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.travelermileage.model.Event;
import com.triple.travelermileage.response.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.net.URL;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewEventIntegrationTest {

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void 리뷰_등록() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/events")
                .content(objectMapper.writeValueAsString(readJson("reviewAdd.json")))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void 리뷰_등록에_따른_포인트_지급() throws Exception {
        String userId = readJson("reviewAdd.json").getUserId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/point/" + userId)).andReturn();
        Message message = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Message.class);

        Assertions.assertThat(Integer.parseInt(String.valueOf(message.getContent()))).isEqualTo(3);
    }

    @Test
    @Order(3)
    void 리뷰_수정() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/events")
                .content(objectMapper.writeValueAsString(readJson("reviewMod.json")))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void 리뷰_수정에_따른_포인트_지급() throws Exception {
        String userId = readJson("reviewMod.json").getUserId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/point/" + userId)).andReturn();
        Message message = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Message.class);

        Assertions.assertThat(Integer.parseInt(String.valueOf(message.getContent()))).isEqualTo(2);
    }

    @Test
    @Order(5)
    void 리뷰_삭제() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/events")
                .content(objectMapper.writeValueAsString(readJson("reviewDelete.json")))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void 리뷰_삭제_포인트_차감() throws Exception {
        String userId = readJson("reviewDelete.json").getUserId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/point/" + userId)).andReturn();
        Message message = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Message.class);

        Assertions.assertThat(Integer.parseInt(String.valueOf(message.getContent()))).isZero();
    }

    private Event readJson(String fileName) throws Exception {
        URL resource = ClassLoader.getSystemClassLoader().getResource("./json/" + fileName);
        File file = new File(resource.getFile());

        return objectMapper.readValue(file, Event.class);
    }
}
