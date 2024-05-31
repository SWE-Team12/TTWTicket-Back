package com.ttwticket.backend.domain.users.service;

import com.ttwticket.backend.domain.fixers.Fixer;
import com.ttwticket.backend.domain.fixers.FixerRepository;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.RecommendDevResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final FixerRepository fixerRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    private List<RecommendDevResponseDto> recommendAlgorithm() {
        Map<Integer, Long> fixerCountMap = new HashMap<>();
        List<Issue> issues = issueRepository.findAll();

        for (Issue issue : issues) {
            List<Fixer> fixers = fixerRepository.findFixerByIssue(issue);
            for (Fixer fixer : fixers) {
                int userId = fixer.getUser().getUserId();
                fixerCountMap.put(userId, fixerCountMap.getOrDefault(userId, 0L) + 1);
            }
        }

        List<Integer> topFixerIds = fixerCountMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return topFixerIds.stream()
                .map(userId -> {
                    User user = userRepository.findByUserIdAndIsDeleted(userId, false);
                    return new RecommendDevResponseDto(user.getUserId(), user.getName(), user.getRole());
                })
                .collect(Collectors.toList());
    }

    public List<RecommendDevResponseDto> recommend() {
        List<Issue> issues = issueRepository.findAll();
        if (issues.isEmpty()) {
            return Collections.emptyList();
        }
        return recommendAlgorithm();
    }
}
