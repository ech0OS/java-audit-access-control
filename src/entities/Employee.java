package entities;

import entities.enums.AccessLevel;
import entities.enums.RiskLevel;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private Integer id;
    private String name;
    private String email;
    private AccessLevel level;
    private boolean active;

    private Department department;
    private List<AuditLog> logs = new ArrayList<>();

    public Employee(){
    }

    public Employee(Integer id, String name, String email, AccessLevel level, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
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

    public AccessLevel getLevel() {
        return level;
    }

    public void setLevel(AccessLevel level) {
        this.level = level;
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
        logs.add(log);
    }

    public boolean hasHighRiskActivity() {
        for(AuditLog log : logs) {
            if(log.getRisk() == RiskLevel.HIGH || log.getRisk() == RiskLevel.CRITICAL) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", active=" + active +
                ", department=" + department +
                ", logs=" + logs +
                '}';
    }
}
