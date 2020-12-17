package poplawski.adam.tuidemo.providers.github;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import poplawski.adam.tuidemo.providers.github.models.GitHubBranch;
import poplawski.adam.tuidemo.providers.github.models.GitHubRepository;

import java.util.List;

@Service
@FeignClient(name = "gitHubFeignClient", url = "${git-hub-api.url}", configuration = GitHubClientConfiguration.class)
public interface GitHubClient {

    @RequestMapping(method = RequestMethod.GET, value = "${git-hub-api.url-repos}")
    List<GitHubRepository> getRepositories(@PathVariable String username);

    @RequestMapping(method = RequestMethod.GET, value = "${git-hub-api.url-branches}")
    List<GitHubBranch> getRepositoriesBranches(@PathVariable String username, @PathVariable String repoName);
}

