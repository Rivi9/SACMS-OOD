package com.sacms.sacmsood.Models;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class AttendanceReportGenerator {

    public static void main(String[] args) {
        try {
            // Load the JasperDesign from the JRXML file
            JasperDesign design = JRXmlLoader.load("src/main/resources/Reports/Attendance_report.jrxml");

            // Compile the JasperDesign into a JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(design);

            // Fill the report and get the JasperPrint object
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, mysqlConnector.getConnection());

            // Save the generated report as a PDF file in the project folder
            String pdfFilePath = "target/Generated reports/Attendance_report.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilePath);

            System.out.println("Report generated and saved as " + pdfFilePath);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
