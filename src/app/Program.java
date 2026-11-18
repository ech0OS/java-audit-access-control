package app;

import entities.*;
import entities.exceptions.AuditException;
import services.AuditFileService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Program {
    public static void main(String[] args) {

        try (Scanner enter = new Scanner(System.in)) {
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.print("Nome do departamento: ");
            String deptName = enter.nextLine();
            Department dept = new Department(deptName);

            System.out.println("\n--- CADASTRO DO FUNCIONÁRIO ---");
            System.out.print("ID do funcionário: ");
            int id = enter.nextInt();
            enter.nextLine();

            System.out.print("Nome: ");
            String name = enter.nextLine();

            System.out.print("Email: ");
            String email = enter.nextLine();

            System.out.print("Perfil (1 - Guest / 2 - Regular / 3 - Admin): ");
            int profileType = enter.nextInt();
            enter.nextLine();

            Employee emp = switch (profileType) {
                case 1 -> {
                    System.out.print("Data de expiração do acesso (dd/MM/yyyy): ");
                    LocalDate expDate = LocalDate.parse(enter.nextLine(), dateFmt);
                    yield new GuestUser(id, name, email, dept, expDate);
                }
                case 2 -> new RegularEmployee(id, name, email, dept);
                case 3 -> {
                    System.out.print("MFA ativado? (true/false): ");
                    boolean mfa = Boolean.parseBoolean(enter.nextLine());
                    yield new adminEmployee(id, name, email, dept, mfa);
                }
                default -> throw new AuditException("Tipo de perfil inválido! Escolha 1, 2 ou 3.");
            };

            System.out.print("\nCaminho do arquivo CSV de logs: ");
            String csvPath = enter.nextLine();

            AuditFileService fileService = new AuditFileService();
            List<String> rejectedAlerts = fileService.loadLogsFromCsv(csvPath, emp);
            String reportPath = fileService.exportAuditReport(csvPath, emp, rejectedAlerts);

            System.out.println("\n--- RESUMO DA AUDITORIA ---");
            System.out.println("Funcionário: " + emp.getName() + " [" + emp.getClass().getSimpleName() + "]");
            System.out.println("Departamento: " + emp.getDepartment().getName());
            System.out.println("Status da conta: " + (emp.isActive() ? "ATIVA" : "BLOQUEADA"));
            System.out.println("Logs aprovados: " + emp.getLogs().size());
            System.out.println("Tentativas bloqueadas/alertas: " + rejectedAlerts.size());
            System.out.println("Possui atividades de alto risco? " + (emp.hasHighRiskActivity() ? "SIM" : "NÃO"));
            System.out.println("\nRelatório gerado com sucesso em: " + reportPath);

        } catch (AuditException e) {
            System.out.println("\n[ERRO DE AUDITORIA]: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("\n[ERRO DE DATA]: Formato inválido. Use dd/MM/yyyy.");
        } catch (InputMismatchException e) {
            System.out.println("\n[ERRO DE ENTRADA]: Você digitou texto em um campo numérico.");
        }
    }
}
