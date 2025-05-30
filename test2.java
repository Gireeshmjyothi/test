package com.yourpackage.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "RECON_FILE_SUMMARY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconFileSummary {

    @Id
    private UUID rfsId;

    private UUID configId;
    private String sftpPath;
    private String s3Path;
    private String fileName;
    private Long bankId;
    private Timestamp fileReceivedTime;
    private Timestamp fileUploadTime;
    private Long totalRecords;
    private BigDecimal totalAmount;
    private String parsingStatus;
    private String matchedRecords;
    private String unmatchedRecords;
    private String duplicateRecords;
    private String reconStatus;
    private Timestamp reconTime;
    private String settlementStatus;
    private Timestamp settlementTime;
    private String remark;
}
