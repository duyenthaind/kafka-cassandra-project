/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.demo.chat;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DuyenThai
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        System.out.println(args);
        new Thread(() -> {
            try {
                KafKaConsumer.runConsumer();
                System.out.println("Running....");
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        System.out.println("Input message: ");
        input = scanner.nextLine();
        if (args.length == 0) {
            KafKaProducer.runProducer(1, input);
        } else {
            KafKaProducer.runProducer(Integer.parseInt(args[0]), input);
        }
    }
}
