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
    byte[] readBuffer;
    public DataCentre()
    {
        portName = "COM3";
        comPortList = SerialPort.getCommPorts();
        setPortFromPortName(portName);
        currentPort.openPort();
        currentPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 500, 0);
        readBuffer = new byte[22];
        try {
            int numRead = currentPort.readBytes(readBuffer, readBuffer.length);
            System.out.println("Read " + numRead + " bytes.");

        } catch (Exception e) { e.printStackTrace(); }

        String message = new String(readBuffer);
        System.out.println(message);
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
