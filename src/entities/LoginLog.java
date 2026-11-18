package entities;

import entities.enums.ActionType;
import entities.enums.RiskLevel;

import java.time.LocalDateTime;

public class LoginLog extends AuditLog{

    private String device;

    public LoginLog(){super();}

    public LoginLog(LocalDateTime timestamp, String ipAddress, boolean success, RiskLevel risk, String description, String device) {
        super(timestamp, ipAddress, (success ? ActionType.LOGIN_SUCESS : ActionType.LOGIN_FAILED), risk, description);
        this.device = device;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String formatForReport() {
        return String.format("[%s] %s | IP: %s | Dispositivo: %s | Risco: %s | %s",
                getAction(),
                getTimestamp().format(FMT),
                getIpAddress(),
                device,
                getRisk(),
                getDescription());
    }
}
