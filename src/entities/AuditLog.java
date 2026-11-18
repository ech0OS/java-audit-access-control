package entities;

import entities.enums.ActionType;
import entities.enums.RiskLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AuditLog {
    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private LocalDateTime timestamp;
    private String ipAddress;
    private ActionType action;
    private RiskLevel risk;
    private String description;

    public AuditLog() {
    }

    public AuditLog(LocalDateTime timestamp, String ipAddress, ActionType action, RiskLevel risk, String description) {
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.action = action;
        this.risk = risk;
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public RiskLevel getRisk() {
        return risk;
    }

    public void setRisk(RiskLevel risk) {
        this.risk = risk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract String formatForReport();
}
