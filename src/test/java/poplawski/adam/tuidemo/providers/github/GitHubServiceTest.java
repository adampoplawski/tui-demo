package poplawski.adam.tuidemo.providers.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import poplawski.adam.tuidemo.models.Account;
import poplawski.adam.tuidemo.models.Branch;
import poplawski.adam.tuidemo.models.Repository;
import poplawski.adam.tuidemo.providers.github.models.GitHubBranch;
import poplawski.adam.tuidemo.providers.github.models.GitHubCommit;
import poplawski.adam.tuidemo.providers.github.models.GitHubOwner;
import poplawski.adam.tuidemo.providers.github.models.GitHubRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    private final GitHubClient gitHubClient = Mockito.mock(GitHubClient.class);

    private GitHubService gitHubService;

    @BeforeEach
    public void setUp() {
        gitHubService = new GitHubService(gitHubClient);
    }

    @Test
    public void getNotForkRepositories_shouldFilterForksAndFetchBranches() {
        //given
        var forkRepo = new GitHubRepository(true, null, new GitHubOwner(), null);
        var nonForkRepo1 = new GitHubRepository(false, null, new GitHubOwner(), null);
        var nonForkRepo2 = new GitHubRepository(false, null, new GitHubOwner(), null);
        var branch = new GitHubBranch(null, new GitHubCommit(null));

        when(gitHubClient.getRepositories(any()))
                .thenReturn(List.of(forkRepo, nonForkRepo1, nonForkRepo2));
        when(gitHubClient.getRepositoriesBranches(any(), any()))
                .thenReturn(List.of(branch));

        //expected
        var expected = Account.builder()
                .userRepositories(List.of(
                        Repository.builder()
                                .branches(List.of(
                                        Branch.builder().build()))
                                .build(),
                        Repository.builder()
                                .branches(List.of(
                                        Branch.builder().build()))
                                .build()))
                .build();

        //when
        Account result = gitHubService.getNotForkRepositories(any());

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        verify(gitHubClient, times(1)).getRepositories(any());
        verify(gitHubClient, times(2)).getRepositoriesBranches(any(), any());
    }

    @Test
    public void getNotForkRepositories_shouldMapModel() {
        //given
        var repositories = List.of(new GitHubRepository(
                false,
                "repoName",
                new GitHubOwner("repoOwner"),
                null));
        var branches = List.of(new GitHubBranch("branchName", new GitHubCommit("sha")));

        when(gitHubClient.getRepositories(any()))
                .thenReturn(repositories);
        when(gitHubClient.getRepositoriesBranches(any(), any()))
                .thenReturn(branches);

        //expected
        var expected = Account.builder()
                .userRepositories(List.of(Repository.builder()
                        .name("repoName")
                        .ownerLogin("repoOwner")
                        .branches(List.of(Branch.builder()
                                .name("branchName")
                                .sha("sha")
                                .build()))
                        .build()))
                .build();

        //when
        Account result = gitHubService.getNotForkRepositories(any());

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        verify(gitHubClient, times(1)).getRepositories(any());
        verify(gitHubClient, times(1)).getRepositoriesBranches(any(), any());
    }
}
