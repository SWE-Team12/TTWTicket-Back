package com.ttwticket.backend.domain.assignee;

import com.ttwticket.backend.domain.issues.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssigneeRepository extends JpaRepository<Assignee, Long> {
    List<Assignee> findAssigneeByIssue(Issue issue);
}
