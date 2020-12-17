package poplawski.adam.tuidemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import poplawski.adam.tuidemo.models.Account;
import poplawski.adam.tuidemo.models.Branch;
import poplawski.adam.tuidemo.models.Repository;
import poplawski.adam.tuidemo.providers.github.models.GitHubBranch;
import poplawski.adam.tuidemo.providers.github.models.GitHubCommit;
import poplawski.adam.tuidemo.providers.github.models.GitHubOwner;
import poplawski.adam.tuidemo.providers.github.models.GitHubRepository;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "git-hub-api.url=localhost:${wiremock.server.port}"
        }
)
@AutoConfigureWireMock(port = 0)
public class GitHubIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAccount_WithUsername_ReturnsAccount() throws JsonProcessingException {
        //given
        String repos = objectMapper.writeValueAsString(List.of(
                new GitHubRepository(false, "repoName", new GitHubOwner(), null),
                new GitHubRepository(false, "repoName", new GitHubOwner(), null)));
        String branch = objectMapper.writeValueAsString(List.of(
                new GitHubBranch(null, new GitHubCommit(null))));

        stubFor(get(urlEqualTo("/users/username/repos")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(repos)));

        stubFor(get(urlEqualTo("/repos/username/repoName/branches")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(branch)));

        //expected
        var expected = Account.builder()
                .userRepositories(List.of(
                        Repository.builder()
                                .name("repoName")
                                .branches(List.of(
                                        Branch.builder().build()))
                                .build(),
                        Repository.builder()
                                .name("repoName")
                                .branches(List.of(
                                        Branch.builder().build()))
                                .build()))
                .build();


        //when
        Account result = testRestTemplate.getForObject("/git-repository/username", Account.class);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }
}
