package com.mycompany.iptracker;

import java.io.IOException;

/**
 *
 * @author Petya
 */
public class IPTrack {

    public static void main(String[] args) throws IOException {
        IPService iPService = new IPService();
        iPService.main();
    }

}
