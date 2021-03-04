package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;
import java.lang.reflect.Array;
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
    private SerialPort currentInputPort;
    private String chosenInputPortName;
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

    private SerialPort currentDCCOutputPort;
    private String chosenOutputPortName;
    private boolean power;
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
        trainList = new ArrayList<>();

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
    public SerialPort getCurrentInputPort()
    {
        return currentInputPort;
    }

    /**
     * Sets the current chosen port.
     * @param currentInputPort The chosen SerialPort.
     */
    public void setCurrentInputPort(SerialPort currentInputPort)
    {
        this.currentInputPort = currentInputPort;
    }

    /**
     * Gets the name of the current port.
     * @return The string that represents the port.
     */
    public String getChosenInputPortName()
    {
        return chosenInputPortName;
    }

    /**
     * Sets the name of the current port.
     * @param portName The String that represents the COM port.
     */
    public void setChosenInputPortName(String portName)
    {
        this.chosenInputPortName = portName;
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
     * A method to set the port that will be used for input by using the port name.
     * @param portWanted The String of the port you want to use.
     */
    public void setInputPortFromPortName(String portWanted)
    {
        for (SerialPort port : comPortList)
        {
            if (port.getSystemPortName().equals(portWanted))
            {
                currentInputPort = port;
            }
        }

    }

    /**
     * A method to set the port that will be used by DCC by using the port name.
     * @param portWanted The String of the port you want to use.
     */
    public void setDCCOuputPortFromPortName(String portWanted)
    {
        for (SerialPort port : comPortList)
        {
            if (port.getSystemPortName().equals(portWanted))
            {
                currentDCCOutputPort = port;
            }
        }

    }

    /**
     * A method to open the chosen input port.
     * @return A boolean to say whether it was successful or not.
     */
    public boolean openSelectedInputPort()
    {
        return currentInputPort.openPort();
    }

    /**
     * A method to open the chosen port to output to DCC controller.
     * @return A boolean to say whether it was successful or not.
     */
    public boolean openSelectedDCCOutputPort()
    {
        return currentDCCOutputPort.openPort();
    }

    /**
     * A method to close the chosen input port.
     * @return A boolean to say whether it was successful or not.
     */
    public boolean closeSelectedInputPort()
    {
        return currentInputPort.closePort();
    }

    /**
     * A method to close the chosen port for DCC output.
     * @return A boolean to say whether it was successful or not.
     */
    public boolean closeSelectedDCCOutputPort()
    {
        return currentDCCOutputPort.closePort();
    }

    /**
     * A method to read the data from the Arduino.
     * @return The String of Data sent to the Serial port.
     */
    public String readDataFromUSB()
    {
        //Make sure that the COM port doesn't timeout
        currentInputPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        //Use an imput Stream with the COM port.

        String wholeMessage ="";
        LocalDateTime messageTime;
        try
        {
            //Try get the first 20 bytes of infomration.
            if (standardParse)
            {
               char a = (char)in.read();
               while (currentInputPort.isOpen())
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
        //currentInputPort.closePort();
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
            wholeMessage = wholeMessage.substring(0,wholeMessage.length()-1);
            String[] informationSent = wholeMessage.split(",");
            System.out.println(wholeMessage);
            addToArraylists(wholeMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wholeMessage;

    }

    /**
     * A method to add message to List of messages received.
     * @param wholeMessage
     */
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

    /**
     * A method to extract the message from the train and store it.
     * @param wholeMessage
     * @return
     */
    public String process (String wholeMessage)
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
        output = ("Train "+ train + " is at location "+ location + ". Max speed is "+ maxSpeed + ". Gradient is "+
                gradient+ ". Length is "+ length+".");
        return output;
    }

    /**
     * A method to clear all data stored.
     */
    public void clearLog()
    {
        inputTime.clear();
        input.clear();
    }

    /**
     * A method to add the new train into the datacentre
     * @param newTrain
     */
    public void addNewTrain(Train newTrain)
    {
        trainList.add(newTrain);
    }

    /**
     * A method to remove a train from the data centre by Train ID
     * @param trainIDtoDelete
     * @return
     */
    public boolean removeTrain(String trainIDtoDelete)
    {
        for(Train train: trainList)
        {
            if(train.getId() == trainIDtoDelete)
            {
                trainList.remove(train);
                return true;
            }
        }
        return false;
    }

    /**
     * A method to get the list of trains.
     * @return
     */
    public ArrayList<Train> getTrainList()
    {
        return trainList;
    }

    /**
     * A method to set the list of trains.
     * @param trainList
     */
    public void setTrainList(ArrayList<Train> trainList)
    {
        this.trainList =  trainList;
    }

    /**
     * A method to get the current port for DCC connection.
     * @return The COM port object
     */
    public SerialPort getCurrentDCCOutputPort() {
        return currentDCCOutputPort;
    }

    /**
     * A method to set the COM port for DCC connection.
     * @param currentDCCOutputPort
     */
    public void setCurrentDCCOutputPort(SerialPort currentDCCOutputPort) {
        this.currentDCCOutputPort = currentDCCOutputPort;
    }

    /**
     * A method to get the name of the COM port for DCC connection.
     * @return name of COM port
     */
    public String getChosenOutputPortName() {
        return chosenOutputPortName;
    }

    /**
     * A method to set the name of the Output COM port.
     * @param chosenOutputPortName
     */
    public void setChosenOutputPortName(String chosenOutputPortName) {
        this.chosenOutputPortName = chosenOutputPortName;
    }

    /**
     * A method to send the power on command to the DCC++ EX Arduino.
     */
    public void togglePowerCommand()
    {
        if(!power)
        {
            String onCommand ="<1>";
            currentDCCOutputPort.writeBytes(onCommand.getBytes(),onCommand.length());
            power = true;
        }
        else
        {
            String offCommand ="<0>";
            currentDCCOutputPort.writeBytes(offCommand.getBytes(),offCommand.length());
            power = false;
        }
    }

    /**
     * A method to see whether the power is on or off.
     * 0 is off, 1 is on.
     * @return power is on or off
     */
    public boolean isPower() {
        return power;
    }

    /**
     * A method to set the power state.
     * 0 is off, 1 is on.
     * @param power
     */
    public void setPower(boolean power) {
        this.power = power;
    }
}
