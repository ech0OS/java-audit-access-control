package entities;

import entities.enums.ActionType;
import entities.enums.RiskLevel;
import entities.exceptions.AuditException;

import java.util.ArrayList;
import java.util.List;

public abstract class Employee {
    private Integer id;
    private String name;
    private String email;
    private boolean active;

    private Department department;
    private List<AuditLog> logs = new ArrayList<>();

    public Employee(){
    }

    public Employee(Integer id, String name, String email, Department department) {
        if(name == null || name.isBlank()) {
            throw new AuditException("O nome do funcionário não pode ser vazio");
        }
        if(email == null || !email.contains("@")) {
            throw new AuditException("Email inválido");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = true;
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<AuditLog> getLogs() {
        return logs;
    }

    public void addLog(AuditLog log){
        if(!active) {
            throw new AuditException("Conta bloqueada (" + name + "): não é possivel registrar novos logs...");
        }
        validateAction(log);
        logs.add(log);
        checkFailedLogin();
    }

    private void checkFailedLogin() {
        int failedCount = 0;
        for (AuditLog log: logs){
            if (log.getAction() == ActionType.LOGIN_FAILED) {
                failedCount++;
            }
        }
        if(failedCount >= getMaxFailedLogins()) {
            this.active = false;
        }
    }

    public boolean hasHighRiskActivity() {
        for(AuditLog log : logs) {
            if(log.getRisk() == RiskLevel.HIGH || log.getRisk() == RiskLevel.CRITICAL) {
                return true;
            }
        }
        return false;
    }

    protected abstract void validateAction(AuditLog log);

    protected abstract int getMaxFailedLogins();

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", department=" + department +
                ", logs=" + logs +
                '}';
    }
}
