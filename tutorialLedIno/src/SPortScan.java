/**
 * Created by Ota on 06.06.2017.
 */

import gnu.io.*; // RXTXcomm libary

import java.util.Enumeration;

public class SPortScan {
    private String activePort;

    public SPortScan(){
        CommPortIdentifier serialPortId;

        Enumeration enumComm;

        enumComm = CommPortIdentifier.getPortIdentifiers(); // get active ports

        /*
            go throught ports and identify the serial
         */
        while(enumComm.hasMoreElements())
        {
            serialPortId = (CommPortIdentifier) enumComm.nextElement(); // get next one
            if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) // check if is serial type
            {
                this.activePort = (String) serialPortId.getName();
            }
        }
    }

    public String getActivePort(){
        return this.activePort;
    }
}
