package entities;

import entities.enums.ActionType;
import entities.exceptions.AuditException;

import java.time.LocalDate;

public class GuestUser extends Employee{
    private LocalDate expirationDate;

    public GuestUser(){super();}

    public GuestUser(Integer id, String name, String email, Department department, LocalDate expirationDate) {
        super(id, name, email, department);
        if(expirationDate == null) {throw new AuditException("Data de expiração inválida");};
        this.expirationDate = expirationDate;
    }

    @Override
    protected void validateAction(AuditLog log) {
        if(LocalDate.now().isAfter(expirationDate)) {
            throw new AuditException("Acesso de visitante expirando em " + expirationDate + ".");
        }
        if(log.getAction() != ActionType.LOGIN_SUCESS && log.getAction() != ActionType.LOGIN_FAILED){
            throw new AuditException("Visitantes só tem permissão para ações de login.");
        }
    }


    @Override
    protected int getMaxFailedLogins() {
        return 2;
    }
}
