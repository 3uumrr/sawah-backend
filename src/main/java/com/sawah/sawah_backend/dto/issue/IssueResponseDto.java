package com.sawah.sawah_backend.dto.issue;

import com.sawah.sawah_backend.enums.IssueStatus;

import java.time.LocalDateTime;

public record IssueResponseDto(
        Long bookingId,
        String issueNumber,
        LocalDateTime issueDate,
        IssueStatus issueStatus,
        String placeNameAr,
        String placeNameEn,
        String placeImageUrl
) {
}
