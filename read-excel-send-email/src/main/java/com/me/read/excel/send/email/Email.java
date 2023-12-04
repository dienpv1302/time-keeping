/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.me.read.excel.send.email;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author dienpv
 */
public class Email {
    public void TestSendEmail(){
        // Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", HOST_NAME);
        props.put("mail.smtp.starttls.enable", "true");        
        props.put("mail.smtp.port", TSL_PORT);

        // get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
            }
        });
        System.out.println("Hihi start");
        // compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECEIVE_EMAIL));
            message.setSubject("Testing Subject");
            message.setText("test hihi");
 
            // send message
            Transport.send(message);
 
            System.out.println("Message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }    

    public static final String HOST_NAME = "smtp.gmail.com"; 
 
    public static final int TSL_PORT = 587; // Port for TLS/STARTTLS
 
    public static final String APP_EMAIL = "baocaotest110@gmail.com"; // your email
 
    public static final String APP_PASSWORD = "znee rczp amrp izoi"; // your password
 
    public static final String RECEIVE_EMAIL = "dienpv@me.com.vn";   
    
//    public static final String HOST_NAME = "mail.me.local"; 
// 
//    public static final int TSL_PORT = 25; // Port for TLS/STARTTLS
// 
//    public static final String APP_EMAIL = "mealerts@me.com.vn"; // your email
// 
//    public static final String APP_PASSWORD = "123456aA@"; // your password
// 
//    public static final String RECEIVE_EMAIL = "dienpv@me.com.vn";  
    
}
