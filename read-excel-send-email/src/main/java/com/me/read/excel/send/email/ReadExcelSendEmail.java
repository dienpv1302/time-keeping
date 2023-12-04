/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.me.read.excel.send.email;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dienpv
 */
public class ReadExcelSendEmail {

    public static void main(String[] args) {
        // args 
        //      1 args: normal mode: import file excel and export result
        //      2 args:
        //          arg1 excel file input or output
        //          arg2 mode: 1 is append
        //                     0 is truncate
        //                     3 is export result 
        boolean isTruncate = true, isImport = true, isExport = true;
        Excel excel = new Excel();
        Database db = new Database();

        System.out.println("Beginning " + LocalDateTime.now());
        Date dnow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        String url = args[0];
        //String url = "D:\\du lieu cham cong 27-30.11.2023.xls";
        var output_url = url.replace(".xls", "_KQ_" + new SimpleDateFormat("yyyyMMddHHmmss").format(dnow) + "_.xls");
        if (args.length > 1) {
            isImport = true;
            isExport = false;
            switch (args[1]) {
                case "1" -> isTruncate = false;
                case "3" -> {
                    isExport = true;
                    isImport = false;
                }
            }
        }

        if (isImport) {
            if (isTruncate) {
                db.Truncate();
            }

            db.Import(excel.Import(url));
        }

        if (isExport) {
            excel.Export(db.Export(), output_url);
        }

        System.out.println("Ending " + LocalDateTime.now());
    }
}
