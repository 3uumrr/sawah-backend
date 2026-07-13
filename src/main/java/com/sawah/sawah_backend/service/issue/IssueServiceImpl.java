package com.sawah.sawah_backend.service.issue;

import com.sawah.sawah_backend.dto.issue.IssueInputDto;
import com.sawah.sawah_backend.enums.IssueStatus;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Issue;
import com.sawah.sawah_backend.models.ServiceRequest;
import com.sawah.sawah_backend.repository.IssueRepository;
import com.sawah.sawah_backend.service.booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final BookingService bookingService;
    @Override
    public Issue findByIssueNumber(String issueNumber) {
        return issueRepository.findByIssueNumber(issueNumber)
                .orElseThrow(() -> new ResourceNotFoundException("issue.not.found"));
    }

    @Override
    public List<Issue> findByTouristId(Long touristId) {
        return issueRepository.findByTouristId(touristId);
    }

    @Override
    public List<Issue> findByTouristIdAndStatus(Long touristId, IssueStatus status) {
        return issueRepository.findByTouristIdAndStatus(touristId, status);
    }

    @Override
    public List<Issue> findAllIssues(IssueStatus status) {
        if (status == null) {
            return issueRepository.findAllWithDetails();
        }

        return issueRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void createIssue(IssueInputDto inputIssue, Long touristId) {

        ServiceRequest  serviceRequest = bookingService.getServiceRequest(inputIssue.bookingId());

        if (serviceRequest != null && !serviceRequest.getTourist().getId().equals(touristId)) {
            throw new BadRequestException("booking.unauthorized.access");
        }

        Issue issue = Issue.builder()
                .description(inputIssue.description())
                .booking(serviceRequest)
                .status(IssueStatus.OPEN)
                .build();


        issueRepository.save(issue);
    }

    @Override
    @Transactional
    public void updateIssue(String issueNumber, IssueStatus newIssueStatus) {
        Issue  issue = findByIssueNumber(issueNumber);

        issue.setStatus(newIssueStatus);
    }


}
