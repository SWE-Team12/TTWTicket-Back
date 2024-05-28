package com.ttwticket.backend.domain.assignees;

import com.ttwticket.backend.domain.issues.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssigneeRepository extends JpaRepository<Assignee, Integer> {
    List<Assignee> findAssigneeByIssue(Issue issue);
}
