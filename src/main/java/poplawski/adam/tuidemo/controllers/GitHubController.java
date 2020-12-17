package poplawski.adam.tuidemo.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import poplawski.adam.tuidemo.models.Account;
import poplawski.adam.tuidemo.services.GitService;

@RestController
@RequiredArgsConstructor
public class GitHubController {

    private final GitService gitService;

    @Operation(
            summary = "Get users git repositories",
            description = "Endpoint to receive users repositories. " +
                    "For each repository branch, api also provides last commit with sha"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 404, message = "Git user do not exists"),
            @ApiResponse(code = 406, message = "Application/xml do not acceptable")
    })

    @GetMapping(value = "/git-repository/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccount(@PathVariable String username) {
        return gitService.getNotForkRepositories(username);
    }
}