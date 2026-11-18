package entities;

import entities.enums.ActionType;
import entities.enums.RiskLevel;

import java.time.LocalDateTime;

public class ConfigChangeLog extends AuditLog{
    private String settingName;
    private String oldValue;
    private String newValue;

    public ConfigChangeLog(){super();}

    public ConfigChangeLog(LocalDateTime timestamp, String ipAddress, RiskLevel risk, String description, String settingName, String oldValue, String newValue) {
        super(timestamp, ipAddress, ActionType.CONFIG_CHANGE, risk, description);

        this.settingName = settingName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String formatForReport() {
        return String.format("[CONFIG_CHANGE] %s | IP: %s | Config: %s (%s -> %s) | Risco: %s | %s",
                getTimestamp().format(FMT),
                getIpAddress(),
                settingName,
                oldValue,
                newValue,
                getRisk(),
                getDescription()
        );
    }
}
