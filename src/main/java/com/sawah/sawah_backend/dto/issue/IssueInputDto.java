package com.sawah.sawah_backend.dto.issue;

public record IssueInputDto(
        Long bookingId,
        String description
) {
}
