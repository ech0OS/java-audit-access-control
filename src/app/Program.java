package app;

import entities.AuditLog;
import entities.Department;
import entities.Employee;
import entities.enums.AccessLevel;
import entities.enums.ActionType;
import entities.enums.RiskLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Program {
    public static void main(String[] args) {
       Scanner enter = new Scanner(System.in);
       DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");




       enter.close();
    }
}
