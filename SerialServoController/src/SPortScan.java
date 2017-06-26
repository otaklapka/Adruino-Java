/**
 * Created by Ota on 06.06.2017.
 */
import gnu.io.*;
import java.util.Enumeration;

public class SPortScan {
    private String activePort;

    public SPortScan(){
        CommPortIdentifier serialPortId;

        Enumeration enumComm;

        enumComm = CommPortIdentifier.getPortIdentifiers();

        while(enumComm.hasMoreElements())
        {
            serialPortId = (CommPortIdentifier)enumComm.nextElement();
            if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                this.activePort = (String) serialPortId.getName();
            }
        }
    }

    public String getActivePort(){
        return this.activePort;
    }
}
