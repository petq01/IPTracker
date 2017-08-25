/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.iptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Petya
 */
public class IPService {

    public List<IPClientObject> main() throws UnknownHostException, IOException {
        List<String> ipAddresses = getIPs();
        List<IPClientObject> objects = new ArrayList<>();
        for (String ipAddress : ipAddresses) {
            IPClientObject object = new IPClientObject();
            object.setIpAddress(ipAddress);
            object.setMacAddress(doCommandMatcher(ipAddress, "arp -a ", "([a-f0-9]{2}\\-){5}([a-f0-9]{2})", 0, ""));
            object.setNameOfPC(doCommandMatcher(ipAddress, "nslookup ", "Name:\\s*([\\w\\-\\.]*)", 1, "null"));
            objects.add(object);
        }
        for (IPClientObject obj : objects) {
            System.out.println(obj);
        }
        return objects;
    }

    public List<String> getIPs() throws IOException {
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

        Pattern pattern
                = Pattern.compile("192\\.168\\.\\d{1,3}.\\d{1,3}");
        // .*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b  -> all ip dynamic & static ips

        Matcher match = pattern.matcher(out);

        String prev = "", pLoc;

        while (match.find()) {
            pLoc = match.group();	// Returns the IP of other hosts
            ipNames.add(pLoc);
        }
        try {
            brArp.close();
        } catch (IOException ex) {
        }

        System.out.println("\n ------------------------ \n\n " + date + "\n\n\n");

        return ipNames;
    }

    public String doCommand(String execString, String ip, String endline) throws IOException {
        Process nsProcess = Runtime.getRuntime().exec(execString + ip);
        BufferedReader br = new BufferedReader(new InputStreamReader(nsProcess.getInputStream()));
        List<String> output = new ArrayList<>();
        String outputString = null;
        while (true) {
            try {
                outputString = br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            output.add(outputString);
            if (outputString == null) {
                break;
            }
        }
        try {
            br.close();
        } catch (IOException ex) {
        }
        return output.toString();
    }

    public String matcher(String out, String regex, int group) {
        Pattern pattern
                = Pattern.compile(regex);
        Matcher match = pattern.matcher(out);
        out = "";
        String prev = "", pLoc;

        if (match.find()) {
            pLoc = match.group(group);
            out = pLoc;
        }
        return out;
    }

    public String doCommandMatcher(String host, String command, String matcher, int group, String printNull) throws SocketException, UnknownHostException, IOException {
        String out = doCommand(command, host, "");
        String pLoc = null;
        if (out != null) {
            pLoc = matcher(out, matcher, group);
        }
        return pLoc;
    }

}
