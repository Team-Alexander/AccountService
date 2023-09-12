package io.github.uptalent.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.uptalent.account.controller.AdminController;
import io.github.uptalent.account.exception.IllegalAdminActionException;
import io.github.uptalent.account.service.AdminService;
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

import static io.github.uptalent.account.utils.MockModelsUtil.*;
import static io.github.uptalent.starter.model.enums.ModerationStatus.ON_MODERATION;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @MockBean
    private AdminService adminService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @DisplayName("Get all users with moderation status successfully")
    public void getAllUsersWithModerationStatusSuccessfully() {
        int page = 1;
        int size = 3;
        String status = ON_MODERATION.name();
        var usersPage = generateUsersWithModerationStatus();

        when(adminService.getUsersWithModerationStatus(page, size, status))
                .thenReturn(usersPage);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/account/admin/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("status", status)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(usersPage.getContent().size())))
                .andExpect(jsonPath("$.totalPages").value(usersPage.getTotalPages()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Block user successfully")
    public void blockUserSuccessfully() {
        var accountModStatusChange = generateAccountModerationStatusChange();

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/admin/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModStatusChange)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("Block user with status BLOCKED")
    public void blockUserWithStatusBlocked() {
        var accountModStatusChange = generateAccountModerationStatusChange();

        doThrow(IllegalAdminActionException.class)
                .when(adminService).blockUser(accountModStatusChange);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/admin/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModStatusChange)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Unblock user successfully")
    public void unblockUserSuccessfully() {
        var accountModStatusChange = generateAccountModerationStatusChange();

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/admin/unblock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModStatusChange)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("Unblock user with status ACTIVE")
    public void unblockUserWithStatusActive() {
        var accountModStatusChange = generateAccountModerationStatusChange();

        doThrow(IllegalAdminActionException.class)
                .when(adminService).unblockUser(accountModStatusChange);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/admin/unblock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModStatusChange)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Activate user successfully")
    public void activateUserSuccessfully() {
        var accountModStatusChange = generateAccountModerationStatusChange();

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/admin/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModStatusChange)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("Activate user with status ACTIVE")
    public void activateUserWithStatusActive() {
        var accountModStatusChange = generateAccountModerationStatusChange();

        doThrow(IllegalAdminActionException.class)
                .when(adminService).activateUser(accountModStatusChange);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/account/admin/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModStatusChange)))
                .andExpect(status().isBadRequest());
    }
}
