package entities;

import entities.enums.ActionType;
import entities.exceptions.AuditException;

public class RegularEmployee extends Employee{

    public RegularEmployee(){super();}

    public RegularEmployee(Integer id, String name, String email, Department department) {
        super(id, name, email, department);
    }

    @Override
    protected void validateAction(AuditLog log) {
        if(log.getAction() == ActionType.CONFIG_CHANGE) { throw new AuditException("Funcionários padrão não têm permissão para alterar configurações.");}
    }

    @Override
    protected int getMaxFailedLogins() {
        return 3;
    }
}
