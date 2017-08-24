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

    public static void main(String[] args) throws IOException {
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
                ex.printStackTrace();
            }
            if (l == null) {
                break;
            }
            out += "\n" + l;
        }

        // A compiled representation of a regular expression
        Pattern pattern
                = Pattern.compile("192\\.168\\.\\d{1,3}.\\d{1,3}");
        // .*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b  -> all ip dynamic & static

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

            printCommand("nslookup ", ip, "");
            printCommand("arp -a ", ip, "---");

        }

    }

    public static void printCommand(String execString, String ip, String endline) throws IOException {
        Process nsProcess = Runtime.getRuntime().exec(execString + ip);
        BufferedReader br = new BufferedReader(new InputStreamReader(nsProcess.getInputStream()));
        String outputString = null;
        while (true) {
            try {
                outputString = br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(outputString);
            if (outputString == null) {
                System.out.println(endline);
                break;
            }
        }
        try {
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
