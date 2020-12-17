package poplawski.adam.tuidemo.providers.github.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubBranch {
    private String name;
    private GitHubCommit commit;
}

