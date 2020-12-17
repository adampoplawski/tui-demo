package poplawski.adam.tuidemo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    private String name;
    private String ownerLogin;
    private List<Branch> branches;
}
