package com.ttwticket.backend.domain.assignees;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssigneeRepository extends JpaRepository<Assignee, Long> {
    List<Assignee> findAssigneeByIssue(Issue issue);
    List<Assignee> findAssigneeByUser(User user);
}
