package com.ttwticket.backend.domain.issues;

import com.ttwticket.backend.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IssueRepository extends JpaRepository<Issue, Long> {
      public Issue findByIssueId(Integer issueId);
      public List<Issue> findByProject_ProjectId(Integer projectId);
      public Issue findByProject_ProjectIdAndIssueId(Integer projectId, Integer issueId);
      public List<Issue> findByProject_ProjectIdAndStatus(Integer projectId, Status status);
      public List<Issue> findByProject_ProjectIdAndUserId(Integer projectId, Integer userId);
//    public List<Issue> findByProjectIdAndStatus(Integer projectId, Status status);
//    public List<Issue> findByProjectIdAndFixer(Integer projectId, User fixer);
//    public List<Issue> findByProjectIdAndReporter(Integer projectId, User reporter);
}
