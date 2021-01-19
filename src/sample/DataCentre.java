package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;
import java.net.*;

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
    private boolean standardParse;
    private ArrayList<Train> trainList;
    private LocalDateTime messageTime;
    private InputStream in;
    private ArrayList<String> inputTime;
    private ArrayList<String> input;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
    private ServerSocket server;
    private Socket connection;
    BufferedReader inputBuffer;
    String output;

    /**
     * Creates the datacentre
     */
    public DataCentre()
    {

        comPortList = SerialPort.getCommPorts();
        comNameList = new String[comPortList.length];
        //Make the names list by going through all COM ports and extracting names into the array.
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
     * Gets whether we're using the new parsing mode.
     * @return
     */
    public boolean isStandardParse() {
        return standardParse;
    }

    /**
     * Sets whether we're using the new parsing mode or not.
     * @param standardParse
     */
    public void setStandardParse(boolean standardParse) {
        this.standardParse = standardParse;
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
        //Make sure that the COM port doesn't timeout
        currentPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        //Use an imput Stream with the COM port.

        String wholeMessage ="";
        LocalDateTime messageTime;
        try
        {
            //Try get the first 20 bytes of infomration.
            if (standardParse)
            {
               char a = (char)in.read();
               while (currentPort.isOpen())
               {
                   wholeMessage = wholeMessage + a;
                   a = (char)in.read();
                   if (a==';')
                   {
                       break;
                   }
                    //Add it to the message
                }
            }
            else
            {
                for (int j = 0; j < 21; ++j)
                {
                    //System.out.print("[");
                    //Add it to the message
                    wholeMessage = wholeMessage + (char)in.read();
                }
            }
            //Close the input Stream.
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //currentPort.closePort();
        //Get the current time and format it to store.
        addToArraylists(wholeMessage);
        return wholeMessage;
    }

    /**
     * A method to get ready for using WIFI.
     */
    public void startWIFIMode()
    {
        try
        {
            //connect = new Socket("192.168.43.63", 23);
            //inWIFI = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            server = new ServerSocket(80);
            connection = server.accept();
            connection.setKeepAlive(true);
            inputBuffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.out.println(connection.isConnected());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * A method to read data over WIFI
     * @return the information from the Train.
     */
    public String readDataOverWIFI() {
        String wholeMessage ="";

        try {
            wholeMessage = inputBuffer.readLine();
            //System.out.println(wholeMessage);
            addToArraylists(wholeMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wholeMessage;

    }

    private void addToArraylists(String wholeMessage)
    {
        messageTime = LocalDateTime.now();
        String timeString = messageTime.format(format);

        //Infomration is stored in an arraylist.
        if (wholeMessage != null)
        {
            wholeMessage = wholeMessage.replace("\n", "").replace("\r", "");
            inputTime.add(timeString);
            input.add(wholeMessage);
            //process(wholeMessage);
        }

    }

    /*public String process (String wholeMessage)
    {
        String[] data = wholeMessage.split(",");
        for(String part : data)
        {
            part = part.replaceAll("[^\\d.]", "");
        }
        String train = data[0];
        String location = data[1];
        String maxSpeed = data[2];
        String gradient = data[3];
        String length = data[4];
        output = ("Train"+ train+ "is at location"+ location+ ". Max speed is "+ maxSpeed+ ". Gradient is "+ gradient+
        ". Length is "+ length+".");
        return output;
    }
    */

    /**
     * A method to clear all data stored.
     */
    public void clearLog()
    {
        inputTime.clear();
        input.clear();
    }
}
