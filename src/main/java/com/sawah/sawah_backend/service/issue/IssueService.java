package com.sawah.sawah_backend.service.issue;

import com.sawah.sawah_backend.dto.issue.IssueInputDto;
import com.sawah.sawah_backend.enums.IssueStatus;
import com.sawah.sawah_backend.models.Issue;

import java.util.List;

public interface IssueService {
    Issue findByIssueNumber(String issueNumber);

    List<Issue> findByTouristId(Long touristId);

    List<Issue> findByTouristIdAndStatus(Long touristId, IssueStatus status);

    List<Issue> findAllIssues(IssueStatus status);

    void createIssue(IssueInputDto issue , Long touristId);
    void updateIssue(String issueNumber ,IssueStatus newIssueStatus);
}
