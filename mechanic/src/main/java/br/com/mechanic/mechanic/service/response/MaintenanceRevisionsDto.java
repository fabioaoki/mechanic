package br.com.mechanic.mechanic.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MaintenanceRevisionsDto {
    private Long scheduledRevisions;
    private Long completedRevisions;
    private Long canceledRevisions;
}
