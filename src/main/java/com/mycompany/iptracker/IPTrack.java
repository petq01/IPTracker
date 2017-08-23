/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.iptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Petya
 */
public class IPTrack {

    public static void main(String[] args) throws Exception {
        Date date = new Date();
        List<String> ipNames = new ArrayList<>();

        Process arpProcess = Runtime.getRuntime().exec("arp -a");

        BufferedReader brArp = new BufferedReader(new InputStreamReader(arpProcess.getInputStream()));

        // String out is used to store output of this command(process)
        String out = "";
        while (true) {
            String l = null;
            try {
                l = brArp.readLine();
            } catch (IOException ex) {
            }
            if (l == null) {
                break;
            }
            out += "\n" + l;
        }

        // A compiled representation of a regular expression
        Pattern pattern
                = Pattern.compile(".*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");

        /* An engine that performs match operations on a character sequence by interpreting a Pattern */
        Matcher match = pattern.matcher(out);

        out = "";
        String prev = "", pLoc;

        if (!(match.find())) // In case no IP address Found in out
        {
            out = "No IP found!";
        } else {

            /* Returns the input subsequence matched by the previous match in this case IP of our interface */
            pLoc = match.group();

            out += pLoc + "\nOther Hosts'(In Same Network) IP addresses:\n";
            while (match.find()) {
                pLoc = match.group();	// Returns the IP of other hosts
                ipNames.add(pLoc);

                out += pLoc + "\n";
            }
            try {
                brArp.close();
            } catch (IOException ex) {
            }
        }
        System.out.println("\n ------------------------ \n\n " + date + "\n\n\n");
        for (String ip : ipNames) {

            BufferedReader brNs = null;
            BufferedReader brPsyhical = null;
            String ns = null;
            String a = null;
            printCommand(ns, brNs, "nslookup ", ip);
            printCommand(a, brPsyhical, "arp -a ", ip);

        }

    }

    public static void printCommand(String l, BufferedReader br, String execString, String ip) throws IOException {
        Process nsProcess = Runtime.getRuntime().exec(execString + ip);
        br = new BufferedReader(new InputStreamReader(nsProcess.getInputStream()));

        while (true) {
            try {
                l = br.readLine();
            } catch (IOException ex) {
            }
            System.out.println(l);
            if (l == null) {
                System.out.println("\n ---- \n");
                break;
            }
        }
        try {
            br.close();
        } catch (IOException ex) {
        }

    }

}
