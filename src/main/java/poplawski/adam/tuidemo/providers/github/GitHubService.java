package poplawski.adam.tuidemo.providers.github;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import poplawski.adam.tuidemo.models.Account;
import poplawski.adam.tuidemo.providers.github.mappers.GitHubMapper;
import poplawski.adam.tuidemo.providers.github.models.GitHubRepository;
import poplawski.adam.tuidemo.services.GitService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitHubService implements GitService {

    private final GitHubClient gitHubClient;

    @Override
    public Account getNotForkRepositories(String username) {
        List<GitHubRepository> repositories = gitHubClient.getRepositories(username);
        repositories = filterForks(repositories);
        return GitHubMapper.toAccount(fetchBranches(username, repositories));
    }

    private List<GitHubRepository> fetchBranches(String username, List<GitHubRepository> repositories) {
        return repositories.parallelStream()
                .map(repository -> mapRepository(username, repository))
                .collect(Collectors.toList());
    }

    private GitHubRepository mapRepository(String username, GitHubRepository repository) {
        return GitHubRepository.builder()
                .name(repository.getName())
                .owner(repository.getOwner())
                .fork(repository.isFork())
                .branches(gitHubClient.getRepositoriesBranches(username, repository.getName()))
                .build();
    }

    private List<GitHubRepository> filterForks(List<GitHubRepository> repositories) {
        return repositories.stream().filter(repository -> !repository.isFork()).collect(Collectors.toList());
    }
}
