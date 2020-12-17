package poplawski.adam.tuidemo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import poplawski.adam.tuidemo.exceptions.GitIntegrationException;
import poplawski.adam.tuidemo.exceptions.GitRequestLimitsExceeded;
import poplawski.adam.tuidemo.exceptions.GitUserNotFoundException;
import poplawski.adam.tuidemo.services.GitService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitService gitService;

    @Test
    public void getAccount_WithCorrectUsername_Returns200() throws Exception {
        //when
        when(this.gitService.getNotForkRepositories("username"))
                .thenReturn(any());

        //then
        this.mockMvc.perform(get("/git-repository/{username}", "username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAccount_NotFound_Returns404() throws Exception {
        //when
        when(this.gitService.getNotForkRepositories(any()))
                .thenThrow(new GitUserNotFoundException("Git user not found"));

        //then
        this.mockMvc.perform(get("/git-repository/{username}", "username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("message").value("Git user not found"));
    }

    @Test
    public void getAccount_AcceptTypeXml_Returns406() throws Exception {
        //then
        this.mockMvc.perform(get("/git-repository/{username}", "username")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }


    @Test
    public void getAccount_NotFound_Returns429() throws Exception {
        //when
        when(this.gitService.getNotForkRepositories(any()))
                .thenThrow(new GitRequestLimitsExceeded("Git request limits exceeded"));

        //then
        this.mockMvc.perform(get("/git-repository/{username}", "username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("status").value(429))
                .andExpect(jsonPath("message").value("Git request limits exceeded"));
    }

    @Test
    public void getAccount_NotFound_Returns503() throws Exception {
        //when
        when(this.gitService.getNotForkRepositories(any()))
                .thenThrow(new GitIntegrationException("Unhandled Git integration error"));

        //then
        this.mockMvc.perform(get("/git-repository/{username}", "username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("status").value(503))
                .andExpect(jsonPath("message").value("Unhandled Git integration error"));
    }
}