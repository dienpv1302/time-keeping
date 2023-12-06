/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.me.read.excel.send.email;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
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
    public void testSend_withpass() {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", "mail.na-me.local");

        // SSL Port
        properties.put("mail.smtp.port", "25");

        // enable authentication
        properties.put("mail.smtp.auth", "true");

        // SSL Factory
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        // creating Session instance referenced to 
        // Authenticator object to pass in 
        // Session.getInstance argument
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {

            // override the getPasswordAuthentication 
            // method
            protected PasswordAuthentication
                    getPasswordAuthentication() {
                return new PasswordAuthentication("na-mealerts@na-me.com.vn",
                        "123456aA");
            }
        });

        //compose the message
        try {
            // javax.mail.internet.MimeMessage class is mostly 
            // used for abstraction.
            MimeMessage message = new MimeMessage(session);

            // header field of the header.
            message.setFrom(new InternetAddress("na-mealerts@na-me.com.vn"));

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("dienpv@na-me.com.vn"));
            message.setSubject("subject");
            message.setText("Hello, 22 is sending email ");

            // Send message
            Transport.send(message);
            System.out.println("Yo it has been sent..");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void testSend_crt() {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", "mail.na-me.local");

        // SSL Port
        properties.put("mail.smtp.port", "25");

        // Enable SSL/TLS
        //properties.put("mail.smtp.starttls.enable", "true");

        // Enable SSL/TLS required for some servers
        //properties.put("mail.smtp.starttls.required", "true");

        // Use SSL socket factory
        properties.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Trust the SSL certificate (use your certificate file and password)
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            
            String certPath = Paths.get(System.getProperty("user.dir"), "mail.na-me.local.crt").toString();
            InputStream certInputStream = new FileInputStream(certPath);
            Certificate cert = certFactory.generateCertificate(certInputStream);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null); // Use null parameters for an empty keystore
            keyStore.setCertificateEntry("na-me-certificate", cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Set the SSLContext in the properties
            properties.put("mail.smtp.ssl.context", sslContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        // Creating Session instance referenced to 
        // Authenticator object to pass in 
        // Session.getInstance argument
        Session session = Session.getDefaultInstance(properties);

        // Compose the message
        try {
            // javax.mail.internet.MimeMessage class is mostly 
            // used for abstraction.
            MimeMessage message = new MimeMessage(session);

            // Header field of the header.
            message.setFrom(new InternetAddress("na-mealerts@na-me.com.vn"));

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("dienpv@na-me.com.vn"));
            message.setSubject("Send email with crt");
            message.setText("Hello, this is a test email with SSL.");

            // Send message
            Transport.send(message);
            System.out.println("Email has been sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
