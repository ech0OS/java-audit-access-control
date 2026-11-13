package app;

import entities.AuditLog;
import entities.Department;
import entities.Employee;
import entities.enums.AccessLevel;
import entities.enums.ActionType;
import entities.enums.RiskLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;


public class Program {
    public static void main(String[] args) {
       Scanner enter = new Scanner(System.in);
       DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

       System.out.print("Nome do departamento: ");
       String dpName = enter.nextLine();

       Department dept = new Department(dpName);

       System.out.println("Entre com os dados do funcionário: ");

        System.out.print("ID do funcionário: ");
        int id = enter.nextInt();
        enter.nextLine();

       System.out.print("Nome: ");
       String funcName = enter.nextLine();

       System.out.print("Email: ");
       String funcEmail = enter.nextLine();

       System.out.print("Nível de acesso (GUEST, USER, ADMIN): ");
       AccessLevel level = AccessLevel.valueOf(enter.nextLine().toUpperCase());

       Employee emp = new Employee(id, funcName, funcEmail, level, dept);

       System.out.print("Quantos registros de auditoria para este funcionário? ");
       int n = enter.nextInt();
       enter.nextLine();

       for(int i = 1; i <= n; i++) {
           System.out.println("Entre com os dados de registro #" + i + ":");

           System.out.print("Data e hora (DD/MM/YYYY HH:mm): ");
           LocalDateTime timestamp = LocalDateTime.parse(enter.nextLine(), fmt);

           System.out.print("Endereço IP: ");
           String ipAddress = enter.nextLine();

           System.out.print("Tipo de ação (LOGIN_SUCESS, LOGIN_FAILED): ");
           ActionType action = ActionType.valueOf(enter.nextLine().toUpperCase());

           System.out.print("Nível de risco (LOW, MEDIUM, HIGH): ");
           RiskLevel risk = RiskLevel.valueOf(enter.nextLine().toUpperCase());

           System.out.print("Descrição: ");
           String description = enter.nextLine();

           AuditLog log = new AuditLog(timestamp, ipAddress, action, risk, description);
           emp.addLog(log);
       }

       System.out.println("\n --- RESUMO DO FUNCIONÁRIO ---");
       System.out.println("Nome: " + emp.getName() + " (" + emp.getEmail() + ")");
       System.out.println("Departamento: " + emp.getDepartment().getName());
       System.out.println("Nível de acesso: " + emp.getLevel());
       System.out.println("Total de logs: " + emp.getLogs());
       System.out.println("Possui atividades de alto risco? " + (emp.hasHighRiskActivity() ? "SIM" : "NÃO"));



       enter.close();
    }
}
