package poplawski.adam.tuidemo.providers.github;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import poplawski.adam.tuidemo.models.Account;
import poplawski.adam.tuidemo.providers.github.mappers.GitHubMapper;
import poplawski.adam.tuidemo.providers.github.models.GitHubBranch;
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
        return GitHubMapper.toAccount(getBranches(username, repositories));
    }

    private List<GitHubRepository> getBranches(String username, List<GitHubRepository> repositories) {
        repositories.forEach(repository -> {
            List<GitHubBranch> repositoriesBranches = gitHubClient.getRepositoriesBranches(username, repository.getName());
            repository.setBranches(repositoriesBranches);
        });
        return repositories;
    }

    private List<GitHubRepository> filterForks(List<GitHubRepository> repositories) {
        return repositories.stream().filter(repository -> !repository.isFork()).collect(Collectors.toList());
    }
}
