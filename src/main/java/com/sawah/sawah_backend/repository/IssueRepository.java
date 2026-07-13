package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.IssueStatus;
import com.sawah.sawah_backend.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {
        @Query("SELECT iss FROM Issue iss " +
                "JOIN FETCH iss.booking b " +
                "LEFT JOIN FETCH b.place pl " +
                "JOIN FETCH b.tourist t " +
                "WHERE t.id = :touristId")
        List<Issue> findByTouristId(@Param("touristId") Long touristId);

        @Query("SELECT iss FROM Issue iss " +
                "JOIN FETCH iss.booking b " +
                "LEFT JOIN FETCH b.place pl " +
                "WHERE iss.issueNumber = :issueNumber")
        Optional<Issue> findByIssueNumber(@Param("issueNumber") String issueNumber);

        @Query("SELECT iss FROM Issue iss " +
                "JOIN FETCH iss.booking b " +
                "LEFT JOIN FETCH b.place pl " +
                "JOIN FETCH b.tourist t " +
                "WHERE t.id = :touristId AND iss.status = :status")
        List<Issue> findByTouristIdAndStatus(
                @Param("touristId") Long touristId,
                @Param("status") IssueStatus status);

        @Query("SELECT iss FROM Issue iss " +
                "JOIN FETCH iss.booking b " +
                "LEFT JOIN FETCH b.place pl " +
                "JOIN FETCH b.tourist t " +
                "LEFT JOIN FETCH b.provider pr " +
                "LEFT JOIN FETCH pr.user u")
        List<Issue> findAllWithDetails();

        @Query("SELECT iss FROM Issue iss " +
                "JOIN FETCH iss.booking b " +
                "LEFT JOIN FETCH b.place pl " +
                "JOIN FETCH b.tourist t " +
                "LEFT JOIN FETCH b.provider pr " +
                "LEFT JOIN FETCH pr.user u " +
                "WHERE iss.status = :status")
        List<Issue> findByStatus(@Param("status") IssueStatus status);

}
