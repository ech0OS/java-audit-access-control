package entities;

import entities.enums.ActionType;
import entities.exceptions.AuditException;

public class adminEmployee extends Employee{
    private boolean mfaEnabled;

    public adminEmployee(){super();}

    public adminEmployee(Integer id, String name, String email, Department department, boolean mfaEnabled) {
        super(id, name, email, department);
        this.mfaEnabled = mfaEnabled;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    @Override
    protected void validateAction(AuditLog log) {
        if(log.getAction() == ActionType.CONFIG_CHANGE && !mfaEnabled) {
            throw new AuditException("Administradores precisam ter o MFA ativado para alterar configurações.");
        }
    }

    @Override
    protected int getMaxFailedLogins() {
        return 5;
    }
}
