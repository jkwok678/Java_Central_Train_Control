package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.ArrayList;

public class DataCentre {

    private SerialPort[] comPortList;
    private SerialPort currentPort;
    private String portName;
    ArrayList<String> wholedata;
    ArrayList<Train> trainList;
    ArrayList<TRack>

    public DataCentre()
    {
        portName = "COM3";
        comPortList = SerialPort.getCommPorts();
        setPortFromPortName(portName);
        currentPort.openPort();
        wholedata = new ArrayList<>();
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
                    if (wholedata.size()<8)
                    {
                        wholedata.add(message);
                    }
                    System.out.print(message);
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
