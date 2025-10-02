/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.me.read.excel.send.email;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
        //                     2 is export result 
        //                     3 is export result persistent 
        boolean isTruncate = true, isImport = true, isExport = true;
        Excel excel = new Excel();
        Database db = new Database();       
        //Email em = new Email();        
        System.out.println("Beginning " + LocalDateTime.now());
        //em.testSend_crt();
        if (args.length == 0) {
            System.out.println("1 args: normal mode: import file excel and export result" );
            System.out.println("	  n args:                                           " );
            System.out.println("		  arg_n-1 excel file input or output               " );
            System.out.println("		  arg_last mode:         1 is append, only import                        " );
            System.out.println("					 0 is truncate, only import                      " );
            System.out.println("					 2 is export result, only export                 " );
            System.out.println("					 3 is export result persistent, only export      " );
            return;
        }
        Date dnow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        String url = args[0];
        //String url = "D:\\du lieu cham cong 27-30.11.2023.xls";
        var output_url = url.replace(".xls", "_KQ_" + new SimpleDateFormat("yyyyMMddHHmmss").format(dnow) + "_.xls");
        if (args.length > 1) {
            isImport = false;
            isExport = false;
            isTruncate = false;
            switch (args[args.length-1]) {
                case "0" -> {isTruncate = true; isImport = true;}
                case "1" -> {isTruncate = false; isImport = true;}
                case "2" -> {
                    isExport = true;
                    isImport = false;
                }
                case "3" -> {
                    isExport = true;
                    isImport = false;
                    db.isConsistent = true;
                }
            }
        }

        if (isImport) {
            if (isTruncate) {
                db.BackUpAndTruncate();
            }
           db.Import(excel.Import(url));
           if(args.length >=3){
               for(int i = 1; i < args.length-1; i++)
                   db.Import(excel.Import(args[i]));
           } 
        }

        if (isExport) {
            excel.Export(db.Export(), output_url);
        }

        System.out.println("Ending " + LocalDateTime.now());
    }
}
