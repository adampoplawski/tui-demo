package poplawski.adam.tuidemo.services;

import poplawski.adam.tuidemo.models.Account;

public interface GitService {
    Account getNotForkRepositories(String username);
}

