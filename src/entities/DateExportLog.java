package entities;

import entities.enums.ActionType;
import entities.enums.RiskLevel;

import java.time.LocalDateTime;

public class DateExportLog extends AuditLog{
    private String tableName;
    private int recordsCount;

    public DateExportLog(){super();}

    public DateExportLog(LocalDateTime timestamp, String ipAddress, RiskLevel risk, String description, String tableName, int recordsCount) {
        super(timestamp, ipAddress, ActionType.DATA_EXPORT, risk, description);
        if(recordsCount > 10000 && (risk == RiskLevel.LOW || risk == RiskLevel.MEDIUM)) {
            setRisk(RiskLevel.HIGH);
        }
        this.tableName = tableName;
        this.recordsCount = recordsCount;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
    }


    @Override
    public String formatForReport() {
        return String.format("[DATA_EXPORT] %s | IP: %s | Tabela: %s (%d registros) | Risco: %s | %s",
                getTimestamp().format(FMT),
                getIpAddress(),
                tableName,
                recordsCount,
                getRisk(),
                getDescription()
        );
    }
}
