package com.vodafone.gno.repository;

import com.vodafone.gno.model.entity.JiraIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JiraIssueRepository extends JpaRepository<JiraIssueEntity, Long> {

    List<JiraIssueEntity> findAllByOrderByCreatedAtDesc();

    Optional<JiraIssueEntity> findByIssueKey(String issueKey);
}
