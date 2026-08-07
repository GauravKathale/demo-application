package com.vodafone.gno.model.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "jira_issue",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"issue_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JiraIssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Jira Identifiers =====
    @Column(name = "issue_id", nullable = false, updatable = false)
    private String issueId;

    @Column(name = "issue_key", nullable = false)
    private String issueKey;

    @Column(name = "project_key")
    private String projectKey;

    // ===== Issue Details =====
    @Column(name = "issue_type")
    private String issueType;

    @Column(length = 500)
    private String summary;

    @Lob
    private String description;

    private String priority;
    private String status;

    private String assignee;
    private String reporter;

    // ===== Sprint Details =====
    private String sprintName;
    private String sprintId;

    // ===== Jira Timestamps =====
    private LocalDateTime createdAtJira;
    private LocalDateTime updatedAtJira;

    // ===== Workflow Integration =====
    private String webhookEvent;

    private String cognitoProcessId;
    private String cognitoTaskId;

    @Builder.Default
    private String processingStatus = "NEW";

    @Builder.Default
    private Integer retryCount = 0;

    // ===== Audit Columns =====
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}