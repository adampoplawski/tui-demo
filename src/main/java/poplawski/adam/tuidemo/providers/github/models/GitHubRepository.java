package poplawski.adam.tuidemo.providers.github.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepository {
    private boolean fork;
    private String name;
    private GitHubOwner owner;
    private List<GitHubBranch> branches;
}
