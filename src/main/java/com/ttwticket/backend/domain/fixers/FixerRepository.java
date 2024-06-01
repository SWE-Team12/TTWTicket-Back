package com.ttwticket.backend.domain.fixers;

import com.ttwticket.backend.domain.issues.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface FixerRepository extends JpaRepository<Fixer, Integer> {
    List<Fixer> findFixerByIssue(Issue issue);
}
