package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *  Location where data is handled and processed
 *  @author Jonathan Kwok
 *  @version 1.0
 *  @since 1.0
 *
 */
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

    /**
     * Creates the datacentre
     */
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

    /**
     * Gets the list of COM ports.
     * @return An array of SerialPorts.
     */
    public SerialPort[] getComPortList()
    {
        return comPortList;
    }

    /**
     * Sets the list of COM ports.
     * @param comPortList Array of SerialPorts.
     */
    public void setComPortList(SerialPort[] comPortList)
    {
        this.comPortList = comPortList;
    }

    /**
     * Gets the list of the COM port names..
     * @return An array of Strings that are the port nanmes
     */
    public String[] getComNameList()
    {
        return comNameList;
    }

    /**
     * Sets the list of COM port names.
     * @param comNameList Array of Strings that represent COM ports.
     */
    public void setComNameList(String[] comNameList)
    {
        this.comNameList = comNameList;
    }

    /**
     * Gets the current chosen port.
     * @return A SerialPort that is to be used.
     */
    public SerialPort getCurrentPort()
    {
        return currentPort;
    }

    /**
     * Sets the current chosen port.
     * @param currentPort The chosen SerialPort.
     */
    public void setCurrentPort(SerialPort currentPort)
    {
        this.currentPort = currentPort;
    }

    /**
     * Gets the name of the current port.
     * @return The string that represents the port.
     */
    public String getChosenPort()
    {
        return chosenPort;
    }

    /**
     * Sets the name of the current port.
     * @param portName The String that represents the COM port.
     */
    public void setChosenPort(String portName)
    {
        this.chosenPort = portName;
    }

    /**
     *  Gets the list of when the Arduino sent data to the program.
     * @return An ArrayList of Strings with the times.
     */
    public ArrayList<String> getInputTime() {
        return inputTime;
    }

    /**
     * Set the list of times when the Arduino sent data to the program.
     * @param inputTime The ArrayList with the times the Arduino sent data.
     */
    public void setInputTime(ArrayList<String> inputTime) {
        this.inputTime = inputTime;
    }

    /**
     * Gets the list of Data that was sent by the Arduino.
     * @return An ArrayList of Strings that the Arduino sent.
     */
    public ArrayList<String> getInput() {
        return input;
    }

    /**
     * Set the list of Data that was sent by the Arduino.
     * @param input An ArrayList of Data sent by the Arduino.
     */
    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    /**
     * A method to set the port that will be used by using the port name.
     * @param portWanted The String of the port you want to use.
     */
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

    /**
     * A method to open the chosen port.
     * @return A boolean to say whether it was successful or not.
     */
    public boolean openSelectedPort()
    {
        return currentPort.openPort();
    }

    /**
     * A method to close the chosen port.
     * @return A boolean to say whether it was successful or not.
     */
    public boolean closeSelectedPort()
    {
        return currentPort.closePort();
    }

    /**
     * A method to read the data from the Arduino.
     * @return The String of Data sent to the Serial port.
     */
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

    /**
     * A method to clear all data stored.
     */
    public void clearLog()
    {
        inputTime.clear();
        input.clear();
    }
}
