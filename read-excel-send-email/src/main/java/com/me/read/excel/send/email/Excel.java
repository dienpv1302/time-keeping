/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.me.read.excel.send.email;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author dienpv
 */
public class Excel {

    public List<Object> Import(String filePath) {
        System.out.println("Start ImportExcel " + LocalDateTime.now());
        List<Object> data = new ArrayList<>();
        int indexOfParenthesis;
        //List<String> dataColumsRevert = new ArrayList<>();

        try {
            // Tạo đối tượng FileInputStream để đọc dữ liệu từ file Excel
            FileInputStream fis = new FileInputStream(filePath);

            // Tạo đối tượng HSSFWorkbook từ FileInputStream
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            // Lấy ra sheet đầu tiên từ Workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                //if(i==8)
                System.out.println("i=" + i);
                String user;
                // Duyệt qua từng ô trong dòng
                Row row = sheet.getRow(i);
                try {
                    user = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
                    indexOfParenthesis = user.indexOf("(");
                    if (indexOfParenthesis < 1) {
                        continue;
                    }
                } catch (Exception ex) {
                    continue;
                }
                Date date = row.getCell(0).getDateCellValue();
                String device = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";

                String In_Out = device;
                device = device.toLowerCase();
                if (device.endsWith(" in")) {
                    In_Out = "IN";
                } else if (device.endsWith(" out")) {
                    In_Out = "OUT";
                } else if (device.contains(" out")) {
                    In_Out = "OUT";
                }

                String datedd = new SimpleDateFormat("yyyyMMdd").format(date);

                List<Object> entry = new ArrayList<>();
                entry.add(user.substring(0, indexOfParenthesis));
                entry.add(datedd);
                entry.add(new SimpleDateFormat("HHmmss").format(date));
                entry.add(In_Out);
                data.add(entry.toArray());
//                if (!dataColumsRevert.contains(datedd)) {
//                    dataColumsRevert.add(datedd);
//                }

            }

            workbook.close();
            fis.close();
            System.out.println("Read success");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

//        dataColumsRevert.add("HT");
//        dataColumsRevert.add("MNV");
//        dataColumsRevert.add("STT1");
//        
//        for(int i = dataColumsRevert.size() - 1; i >=0;i++)
//            dataColums.add(dataColumsRevert.get(i));
        System.out.println("End ImportExcel " + LocalDateTime.now());
        return data;
    }

    public void Export(List<Object> data, String filePath) {
        System.out.println("Start ExportExcel " + LocalDateTime.now());
        try {
            Workbook workbook = new HSSFWorkbook(); // HSSFWorkbook for Excel 97-2003 format
            Sheet sheet = workbook.createSheet("TH");
            // Create header row
            Row headerRow = sheet.createRow(0);
            sheet.setColumnWidth(0, 256 * 4); // STT 3 character
            sheet.setColumnWidth(2, 256 * 6); // MNV 4 character
            sheet.setColumnWidth(1, 256 * 24); // HT 21 character

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle cellStyleBold = workbook.createCellStyle();
            cellStyleBold.setFont(headerFont);

            CellStyle cellStyleBoldWrap = workbook.createCellStyle();
            cellStyleBoldWrap.setFont(headerFont);
            cellStyleBoldWrap.setWrapText(true);

            CellStyle cellStyleWrap = workbook.createCellStyle();
            cellStyleWrap.setWrapText(true);
            
            Object[] dataColums = (Object[]) data.get(0);
            
            for (int i = 3; i < dataColums.length; i++) {
                sheet.setColumnWidth(i, 256 * 30);
            }

            for (int i = 0; i < dataColums.length; i++) {
                Cell headerCell = headerRow.createCell(i);

                headerCell.setCellStyle(cellStyleBold);
                headerCell.setCellValue((String)dataColums[i]);
            }

            for (int i = 1; i < data.size(); i++) {
                Object[] item = (Object[]) data.get(i);
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < dataColums.length; j++) {
                    Cell cell = row.createCell(j);
                    if (j == 1 && item[0].toString().equals("0")) //Phong
                    {
                        cell.setCellStyle(cellStyleBoldWrap);
                    } else {
                        cell.setCellStyle(cellStyleWrap);
                    }

                    cell.setCellValue(item[j] != null ? item[j].toString() : "");
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("End ExportExcel " + LocalDateTime.now());
    }
}
