/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.iptracker;

/**
 *
 * @author Petya
 */
public class IPClientObject {

    private String ipAddress;
    private String macAddress;
    private String nameOfPC;

    @Override
    public String toString() {
        return "\n" + ipAddress + "\n" + macAddress + "\n" + nameOfPC;
    }

    public IPClientObject() {
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getNameOfPC() {
        return nameOfPC;
    }

    public void setNameOfPC(String nameOfPC) {
        this.nameOfPC = nameOfPC;
    }
}
