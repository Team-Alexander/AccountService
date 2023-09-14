package io.github.uptalent.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.uptalent.account.controller.SponsorController;
import io.github.uptalent.account.exception.SponsorNotFoundException;
import io.github.uptalent.account.exception.TalentNotFoundException;
import io.github.uptalent.account.service.AccountService;
import io.github.uptalent.account.service.ReportService;
import io.github.uptalent.account.service.SponsorService;
import io.github.uptalent.starter.model.request.ReportRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static io.github.uptalent.account.utils.MockModelsUtil.generateReportRequest;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SponsorController.class)
public class SponsorControllerTest {
    @MockBean
    private AccountService accountService;
    @MockBean
    private SponsorService sponsorService;
    @MockBean
    private ReportService reportService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @DisplayName("Report Sponsor successfully")
    public void reportSponsorSuccessfully() {
        Long sponsorId = 1L;
        ReportRequest reportRequest = generateReportRequest();

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/sponsors/{id}/report", sponsorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("Report Sponsor that is not found")
    public void reportSponsorThatIsNotFound() {
        Long sponsorId = 1L;
        ReportRequest reportRequest = generateReportRequest();

        doThrow(SponsorNotFoundException.class)
                .when(reportService).reportSponsor(sponsorId, reportRequest);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/sponsors/{id}/report", sponsorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isNotFound());
    }
}
