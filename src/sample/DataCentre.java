package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataCentre {

    private SerialPort[] comPortList;
    private String[] comNameList;
    private SerialPort currentPort;
    private String chosenPort;
    ArrayList<Train> trainList;
    LocalDateTime messageTime;
    InputStream in;
    ArrayList<String> inputTime;
    ArrayList<String> input;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    int x =0;

    public DataCentre()
    {
        comPortList = SerialPort.getCommPorts();
        comNameList = new String[comPortList.length];
        for (int i = 0; i<comPortList.length ; i++)
        {
            comNameList[i] = comPortList[i].getSystemPortName();
        }
        inputTime = new ArrayList<>();
        input = new ArrayList<>();
    }

    public SerialPort[] getComPortList()
    {
        return comPortList;
    }

    public void setComPortList(SerialPort[] comPortList)
    {
        this.comPortList = comPortList;
    }

    public String[] getComNameList()
    {
        return comNameList;
    }

    public void setComNameList(String[] comNameList)
    {
        this.comNameList = comNameList;
    }

    public SerialPort getCurrentPort()
    {
        return currentPort;
    }

    public void setCurrentPort(SerialPort currentPort)
    {
        this.currentPort = currentPort;
    }

    public String getChosenPort()
    {
        return chosenPort;
    }

    public void setChosenPort(String portName)
    {
        this.chosenPort = portName;
    }

    public ArrayList<String> getInputTime() {
        return inputTime;
    }

    public void setInputTime(ArrayList<String> inputTime) {
        this.inputTime = inputTime;
    }

    public ArrayList<String> getInput() {
        return input;
    }

    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    public void setPortFromPortName(String portWanted)
    {
        for (SerialPort port : comPortList)
        {
            if (port.getSystemPortName().equals(portWanted))
            {
                currentPort = port;
            }
        }

    }

    public boolean openSelectedPort()
    {
        return currentPort.openPort();
    }

    public boolean closeSelectedPort()
    {
        return currentPort.closePort();
    }

    public String readDataFromUSB()
    {
        currentPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        in = currentPort.getInputStream();
        String wholeMessage ="";
        LocalDateTime messageTime;
        try
        {
            for (int j = 0; j < 21; ++j)
                //System.out.print("[");
                wholeMessage = wholeMessage + (char)in.read();
            //System.out.print("]");
            in.close();
        } catch (Exception e) { e.printStackTrace(); }
        //currentPort.closePort();
        messageTime = LocalDateTime.now();
        String timeString = messageTime.format(dtf);
        inputTime.add(timeString);
        input.add(wholeMessage);
        return wholeMessage;
    }

    public void clearLog()
    {
        inputTime.clear();
        input.clear();
    }
}
