package entities.exceptions;

public class AuditException extends RuntimeException{
        private static final long serialVersionUID = 1L;

        public AuditException(String msg){
            super(msg);
        }
}
