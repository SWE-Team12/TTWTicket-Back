package com.ttwticket.backend.domain.fixers.service;

import com.ttwticket.backend.domain.fixers.Fixer;
import com.ttwticket.backend.domain.fixers.FixerRepository;
import com.ttwticket.backend.domain.fixers.dto.FixerRequestDto;
import com.ttwticket.backend.domain.fixers.dto.FixerResponseDto;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FixerService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final FixerRepository fixerRepository;

    @Transactional
    public FixerResponseDto assignUser(FixerRequestDto fixerRequestDto, Integer issueId) {
        User user = userValid(fixerRequestDto.getEmail());
        Issue issue = issueValid(issueId);

        FixerResponseDto fixerResponseDto = FixerResponseDto.builder()
                .fixer(fixerRepository.save(new Fixer(user, issue)))
                .build();
        return fixerResponseDto;
    }

    @Transactional
    public List<FixerResponseDto> getUsers(Integer issueId) throws SQLException {
        Issue issue = issueValid(issueId);
        List<Fixer> fixers = fixerRepository.findFixerByIssue(issue);
        return fixers.stream()
                .map(fixer -> FixerResponseDto.builder().fixer(fixer).build())
                .collect(Collectors.toList());
    }


    public Issue issueValid(Integer issueId) {
        return issueRepository.findByIssueId(issueId);
    }

    public User userValid(String email) {
        return userRepository.findByEmailAndIsDeleted(email, false);
    }
}
