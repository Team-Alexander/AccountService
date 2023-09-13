package io.github.uptalent.account;

import io.github.uptalent.account.controller.SkillController;
import io.github.uptalent.account.model.response.SkillResponse;
import io.github.uptalent.account.service.SkillService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static io.github.uptalent.account.utils.MockModelsUtil.generateSkillResponses;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SkillController.class)
public class SkillControllerTest {
    @MockBean
    private SkillService skillService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    @DisplayName("Get all skills successfully")
    public void getAllSkillsSuccessfully() {
        List<SkillResponse> skillResponses = generateSkillResponses();

        when(skillService.getAllSkills()).thenReturn(skillResponses);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/account/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(skillResponses.size())));
    }
}
