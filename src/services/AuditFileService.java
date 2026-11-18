package services;

import entities.*;
import entities.enums.RiskLevel;
import entities.exceptions.AuditException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AuditFileService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public List<String> loadLogsFromCsv(String filePath, Employee emp) {
        List<String> rejectedAlerts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line = br.readLine();
            int lineNumber = 1;

            while(line != null) {

                if(!line.isBlank()) {
                    try {
                        String[] fields = line.split(",");

                        String type = fields[0].trim().toUpperCase();
                        LocalDateTime timestamp = LocalDateTime.parse(fields[1].trim(), FMT);
                        String ipAddress = fields[2].trim();
                        RiskLevel risk = RiskLevel.valueOf(fields[3].trim().toUpperCase());
                        String description = fields[4].trim();

                        AuditLog log = switch(type) {
                            case "LOGIN" -> {
                                boolean success = Boolean.parseBoolean(fields[5].trim());
                                String device = fields[6].trim();
                                yield new LoginLog(timestamp, ipAddress, success, risk, description, device);
                            }
                            case "EXPORT" -> {
                                String tableName = fields[5].trim();
                                int recordsCount = Integer.parseInt(fields[6].trim());
                                yield new DateExportLog(timestamp, ipAddress, risk, description, tableName, recordsCount);
                            }
                            case "CONFIG" -> {
                                String settingName = fields[5].trim();
                                String oldValue = fields[6].trim();
                                String newValue = fields[7].trim();
                                yield new ConfigChangeLog(timestamp, ipAddress, risk, description, settingName, oldValue, newValue);
                            }
                            default -> throw new AuditException("Tipo de log desconhecido: " + type);
                        };
                        emp.addLog(log);
                    } catch (AuditException | DateTimeParseException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                        rejectedAlerts.add("Linha " + lineNumber + " rejeitada: " + e.getMessage());
                    }
                }

                line = br.readLine();
                lineNumber++;
            }
        } catch (IOException e) {
            throw new AuditException("Erro ao abrir/ler o arquivo csv: " + e.getMessage());
        }

        return rejectedAlerts;
    }

    public String exportAuditReport(String sourceFilePath, Employee emp, List<String> rejectedAlerts){
        File sourceFile = new File(sourceFilePath);

        String parentDir = (sourceFile.getParent() != null) ? sourceFile.getParent() : ".";
        String targetFolder = parentDir + File.separator + "out";

        new File(targetFolder).mkdir();

        String targetFilePath = targetFolder + File.separator + "audit report" + emp.getId() + " .txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFilePath))) {
            bw.write("==========================================");
            bw.newLine();
            bw.write("        RELATÓRIO DE AUDITORIA");
            bw.newLine();
            bw.write("==========================================");
            bw.newLine();
            bw.write("Funcionário: " + emp.getName() + " (ID: " + emp.getId() + ")");
            bw.newLine();
            bw.write("E-mail: " + emp.getEmail());
            bw.newLine();
            bw.write("Perfil " + emp.getClass().getSimpleName());
            bw.newLine();
            bw.write("Departamento: " + emp.getDepartment().getName());
            bw.newLine();
            bw.write("Status da conta " + (emp.isActive() ? "ATIVA" : "BLOQUEADA"));
            bw.newLine();
            bw.write("Atividade de Alto Risco: " + (emp.getLogs().size() + ") ---"));
            bw.newLine();
            bw.newLine();

            bw.write("--- LOGS APROVADOS (" + emp.getLogs().size() + ") ---");
            bw.newLine();
            for (AuditLog log : emp.getLogs()) {
                bw.write(log.formatForReport());
                bw.newLine();
            }

            if(!rejectedAlerts.isEmpty()) {
                bw.newLine();
                bw.write("--- ALERTAS / TENTATIVAS BLOQUEADAS (" + rejectedAlerts.size() + ") ---");
                bw.newLine();
                for (String alert : rejectedAlerts) {
                    bw.write(alert);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            throw new AuditException("Erro ao gravar relatório de auditoria: " + e.getMessage());
        }

        return targetFilePath;
    }

}
