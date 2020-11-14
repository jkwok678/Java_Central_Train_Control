package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.ArrayList;

public class DataCentre {

    private SerialPort[] comPortList;
    private SerialPort currentPort;
    private String portName;
    ArrayList<Train> trainList;
    String lastLocation ="";
    int x =0;

    public DataCentre()
    {
        portName = "COM3";
        comPortList = SerialPort.getCommPorts();
        setPortFromPortName(portName);
        currentPort.openPort();
        currentPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED)
                {
                    byte[] newData = event.getReceivedData();
                    String message = new String(newData);
                    //String[] parts = message.split(",");
                    //lastLocation =  parts[0];
                    //String message2 = message.substring(0,22);
                    //System.out.print(lastLocation);
                    //if (!lastLocation.equals(message2))
                    //{
                    //    System.out.print(message2);
                    //    lastLocation = message2;
                    //}
                    /*if (x<8)
                    {
                        lastLocation = lastLocation + message;
                        x++;
                    }
                    else
                    {
                        System.out.print(lastLocation);
                        lastLocation ="";
                        x=0;
                    }
                    */


                    //System.out.print("[ ");
                    System.out.print(message);
                    x++;
                    if (x>8)
                    {
                        System.out.println();
                        x = 0;
                    }
                    //System.out.print(" ]");
                }

            }
        });

    }

    public SerialPort[] getComPortList() {
        return comPortList;
    }

    public void setComPortList(SerialPort[] comPortList) {
        this.comPortList = comPortList;
    }
    public SerialPort getCurrentPort() {
        return currentPort;
    }

    public void setCurrentPort(SerialPort currentPort) {
        this.currentPort = currentPort;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public void setPortFromPortName(String portWanted) {
        for (SerialPort port : comPortList)
        {
            if (port.getSystemPortName().equals(portWanted))
            {
                currentPort = port;
            }
        }

    }
}
