package poplawski.adam.tuidemo.providers.github.mappers;

import poplawski.adam.tuidemo.models.Account;
import poplawski.adam.tuidemo.models.Branch;
import poplawski.adam.tuidemo.models.Repository;
import poplawski.adam.tuidemo.providers.github.models.GitHubBranch;
import poplawski.adam.tuidemo.providers.github.models.GitHubRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GitHubMapper {

    public static Account toAccount(List<GitHubRepository> gitRepositories) {
        return Account.builder()
                .userRepositories(getRepositories(gitRepositories))
                .build();
    }

    private static List<Repository> getRepositories(List<GitHubRepository> gitRepositories) {
        ArrayList<Repository> repositories = new ArrayList<>();
        gitRepositories.forEach(gitRepository ->
                repositories.add(getRepository(gitRepository))
        );
        return repositories;
    }

    private static Repository getRepository(GitHubRepository gitRepository) {
        return Repository.builder()
                .name(gitRepository.getName())
                .ownerLogin(gitRepository.getOwner().getLogin())
                .branches(getBranches(gitRepository.getBranches()))
                .build();
    }

    private static List<Branch> getBranches(List<GitHubBranch> gitBranches) {
        return gitBranches.stream()
                .map(gitBranch -> Branch.builder()
                        .name(gitBranch.getName())
                        .sha(gitBranch.getCommit().getSha())
                        .build())
                .collect(Collectors.toList());
    }
}
