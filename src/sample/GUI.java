package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/** Creates the layout of the GUI.
 * @author Jonathan Kwok
 * @version 1.0
 * @since 1.0
 */
public class GUI {

    private Text title;
    private Button addTrain;
    private Button removeTrain;
    private Popup addTrainForm;
    private HBox USBLayout;
    private RadioButton USBMode;
    private ComboBox USBInputChooser;
    private RadioButton WIFIMode;
    private final ToggleGroup USBWIFIGroup = new ToggleGroup();
    private Button startStop;
    boolean go;
    private HBox parseLayout;
    private RadioButton UIDParse;
    private RadioButton newDataParse;
    private final ToggleGroup dataGroup = new ToggleGroup();
    private DataCentre dataCentre;
    private Text inputText;

    private Text confirmationError;
    private TextArea input;
    private ComboBox USBOutputDCCChooser;
    private Text outputText;
    private TextArea output;
    private Text trainText;
    private TextArea trainOutput;
    private Label sendDCCLabel;
    private Text confirmationError2;
    private HBox sendDCCHBox;
    private TextField sendDCCTextField;
    private Button sendDCCButton;
    private VBox layout;
    private Button powerDCCButton;
    private EventHandler InputEvent;

    /**
     * A method to create the GUI object.
     * @param dataEntry
     */
    public GUI(DataCentre dataEntry)
    {
        dataCentre = dataEntry;
        makeTop();
        makeBottom();
        addChildren();
    }

    /**
     * A method to make a new form to add train.
     */
    public void handleAddTrainButton()
    {
        Stage addTrainStage = new Stage();
        addTrainStage.setTitle("Add new Train");
        GridPane newTrainFormLayout = new GridPane();
        Label trainDetail = new Label("Train details");
        GridPane.setConstraints(trainDetail, 0, 0);
        newTrainFormLayout.getChildren().add(trainDetail);


        Label idLabel = new Label("train ID (000-999): ");
        TextField idTextField = new TextField ();
        GridPane.setConstraints(idLabel, 0, 1);
        newTrainFormLayout.getChildren().add(idLabel);
        GridPane.setConstraints(idTextField, 1, 1);
        newTrainFormLayout.getChildren().add(idTextField);


        Label nameLabel = new Label("train name: ");
        TextField nameTextField = new TextField();
        GridPane.setConstraints(nameLabel, 0, 2);
        newTrainFormLayout.getChildren().add(nameLabel);
        GridPane.setConstraints(nameTextField, 1, 2);
        newTrainFormLayout.getChildren().add(nameTextField);


        Label maxAccelerationLabel = new Label("train max Acceleration: ");
        TextField maxAccelerationTextField = new TextField();
        GridPane.setConstraints(maxAccelerationLabel, 0, 3);
        newTrainFormLayout.getChildren().add(maxAccelerationLabel);
        GridPane.setConstraints(maxAccelerationTextField, 1, 3);
        newTrainFormLayout.getChildren().add(maxAccelerationTextField);


        Label maxDecelerationLabel = new Label("train max deceleration: ");
        TextField maxDecelerationTextField = new TextField();
        GridPane.setConstraints(maxDecelerationLabel, 0, 4);
        newTrainFormLayout.getChildren().add(maxDecelerationLabel);
        GridPane.setConstraints(maxDecelerationTextField, 1, 4);
        newTrainFormLayout.getChildren().add(maxDecelerationTextField);


        Label topSpeedLabel = new Label("train top speed(1-999): ");
        TextField topSpeedTextField = new TextField ();
        GridPane.setConstraints(topSpeedLabel, 0, 5);
        newTrainFormLayout.getChildren().add(topSpeedLabel);
        GridPane.setConstraints(topSpeedTextField, 1, 5);
        newTrainFormLayout.getChildren().add(topSpeedTextField);

        Button confirmTrain = new Button("Add");
        GridPane.setConstraints(confirmTrain,1,6);
        newTrainFormLayout.getChildren().add(confirmTrain);
        Label errorLabel = new Label();
        GridPane.setConstraints(errorLabel,0,7,3,1);
        newTrainFormLayout.getChildren().add(errorLabel);
        errorLabel.setVisible(false);
        confirmTrain.setOnAction(actionEvent2 -> {
            String errorMessage = "";
            boolean complete = true;
            int trainId;
            if(!idTextField.getText().matches("\\d{3}"))
            {
                idTextField.setText("");
                errorMessage = "Needs to be 3 digits integer, with leading zeroes. e.g.(001,010,100)";
                complete = false;
            }
            double maxAcceleration=0;
            double maxDeceleration=0;
            int topSpeed=0;
            try
            {
                maxAcceleration = Double.parseDouble(maxAccelerationTextField.getText());
                maxDeceleration = Double.parseDouble(maxDecelerationTextField.getText());
                topSpeed = Integer.parseInt(topSpeedTextField.getText());
            }
            catch(NumberFormatException e)
            {
                errorMessage = errorMessage+ "\n Max acceleration, max deceleration and top speed needs to be " +
                        "larger than 0. Acceleration and Deceleration can be to 2d.p. with 3 digits";
                complete = false;
            }

            if (!complete)
            {
                errorLabel.setText(errorMessage);
                errorLabel.setVisible(true);
            }
            else
            {
                errorLabel.setVisible(false);
                Train newTrain = new Train(idTextField.getText(), nameTextField.getText(),maxAcceleration, maxDeceleration, topSpeed);
                dataCentre.addNewTrain(newTrain);
                addTrainStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Train created!");
                alert.setContentText("Train has been added!");
                alert.showAndWait();
                String trainString ="";
                for (Train train: dataCentre.getTrainList())
                {
                    trainString = trainString+ "Train ID: " + train.getId() +" Train name: " + train.getName() + "\n";
                }
                trainOutput.setText(trainString);
            }


        });


        Scene popUpAddTrainScene = new Scene(newTrainFormLayout,600,400);
        addTrainStage.setScene(popUpAddTrainScene);
        if (!addTrainStage.isShowing())
        {
            addTrainStage.show();
        }
    }

    /**
     * A method to handle choosing USB or WIFI.
     */
    public void handleUSBWIFIGroup()
    {
        if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("USB"))
        {
            //Create ComboBox with options of COM ports.
            USBInputChooser.setItems(FXCollections
                    .observableArrayList(dataCentre.getComNameList()));
            USBInputChooser.getSelectionModel().selectFirst();
            dataCentre.setChosenInputPortName(USBInputChooser.getValue().toString());
            dataCentre.setInputPortFromPortName(USBInputChooser.getValue().toString());
            USBInputChooser.setDisable(false);
            USBInputChooser.setVisible(true);

        }
        if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("WIFI"))
        {
            USBInputChooser.setDisable(true);
            USBInputChooser.setVisible(false);
            dataCentre.setChosenInputPortName("");
            dataCentre.setCurrentInputPort(null);
        }
    }

    /**
     * A method to handle choosing how to parse data.
     * Tell the program what data to expect.
     */
    public void handleDataGroup(Toggle n)
    {
        if (n == newDataParse)
        {
            dataCentre.setStandardParse(true);
        }
        else
        {
            dataCentre.setStandardParse(false);
        }
    }

    /**
     * A method to handle choosing what USB to use.
     */
    public void handleUSBInputChooserInput(String newValue)
    {
        dataCentre.setChosenInputPortName(newValue);
        dataCentre.setInputPortFromPortName(newValue);
    }


    /**
     * A method to handle starting and stopping a program.
     */
    public void handleStartStop()
    {
        if (startStop.getText().equals("Start"))
        {
            startStop.setText("Stop");
            String port = dataCentre.getChosenInputPortName();
            if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("WIFI"))
            {
                dataCentre.startWIFIMode();
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    if (startStop.getText().equals("Stop"))
                    {
                        if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("USB"))
                        {
                            if (dataCentre.openSelectedInputPort())
                            {
                                //Read from USB

                                confirmationError.setText(port + " is opened.");
                                dataCentre.readDataFromUSB();
                            }
                            else
                            {
                                confirmationError.setText("Cannot connect to" + port);
                            }
                        }
                        else
                        {
                            dataCentre.readDataOverWIFI();
                        }
                        String message = "";
                        //Create an interface that is like a log for the user to see.
                        ArrayList<String> timeList = dataCentre.getInputTime();
                        ArrayList<String> inputList = dataCentre.getInput();
                        for (int i = 0; i< timeList.size(); i++)
                        {
                            message = message + timeList.get(i) + " : " + inputList.get(i) + "\n";
                        }
                        int caretPosition = input.caretPositionProperty().get();
                        input.setText(message);
                        input.positionCaret(input.getLength());
                    }
                }
            },0,250);
        }
        else
        {
            startStop.setText("Start");
            go = false;
            if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("USB"))
            {
                if (dataCentre.closeSelectedInputPort())
                {
                    confirmationError.setText("Connection to" + dataCentre.getChosenInputPortName() + " is closed");
                }
            }
            else
            {

            }


        }

    }


    /**
     * A method to handle choosing what USB to use.
     */
    public void handleSendDCCCommand(String commandToSend)
    {
        String port = dataCentre.getChosenOutputPortName();
        if (dataCentre.openSelectedDCCOutputPort())
        {
            //Read from USB

            if (dataCentre.sendInstruction(commandToSend))
            {
                dataCentre.saveLastDCCCommand(commandToSend);
                confirmationError2.setText(port + " is opened for DCC connection."+ "Sent " + commandToSend);
            }

        }
        else
        {
            confirmationError2.setText("Cannot connect to" + port + " for DCC connection.");
        }
    }
    /**
     * A method to toggle power on and off for DCC controller.
     */
    public void handleTogglePower()
    {

        dataCentre.togglePowerCommand();
        if (dataCentre.isPower())
        {
            powerDCCButton.setText("Off");
        }
        else
        {
            powerDCCButton.setText("On");
        }


    }
    /**
     * A method to create the top half of the GUI.
     */
    private void makeTop()
    {

        title = new Text("Train control");
        addTrain = new Button("Add Train");

        addTrain.setOnAction(actionEvent -> handleAddTrainButton());
        removeTrain = new Button("Remove Train");
        removeTrain.setVisible(false);


        USBLayout = new HBox();
        USBMode = new RadioButton("USB mode");
        USBMode.setToggleGroup(USBWIFIGroup);
        USBMode.setUserData("USB");
        USBInputChooser = new ComboBox();
        USBInputChooser.setDisable(true);
        USBInputChooser.setVisible(false);
        USBLayout.getChildren().add(USBMode);
        USBLayout.getChildren().add(USBInputChooser);
        WIFIMode = new RadioButton("WIFI mode");
        WIFIMode.setToggleGroup(USBWIFIGroup);
        WIFIMode.setUserData("WIFI");
        USBWIFIGroup.selectedToggleProperty().addListener((o,oldVal,newVal)->handleUSBWIFIGroup());

        parseLayout = new HBox();
        UIDParse = new RadioButton("UID mode");
        newDataParse = new RadioButton("New Data mode");
        UIDParse.setToggleGroup(dataGroup);
        newDataParse.setToggleGroup(dataGroup);
        dataGroup.selectToggle(newDataParse);
        dataCentre.setStandardParse(true);
        parseLayout.getChildren().add(UIDParse);
        parseLayout.getChildren().add(newDataParse);
        dataGroup.selectedToggleProperty().addListener((ob,o,n)-> handleDataGroup(n));


        USBInputChooser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldvalue, final String newvalue)
            {
                dataCentre.setChosenInputPortName(newvalue);
                dataCentre.setInputPortFromPortName(newvalue);
            }
        });


        startStop = new Button("Start");
        startStop.setOnAction(actionEvent -> handleStartStop());
    }


    private void makeUSBOutputDCCChooser()
    {
        USBOutputDCCChooser.setItems(FXCollections
                .observableArrayList(dataCentre.getComNameList()));
        USBOutputDCCChooser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldvalue, final String newvalue)
            {
                dataCentre.setChosenOutputPortName(newvalue);
                dataCentre.setDCCOuputPortFromPortName(newvalue);
                String port = dataCentre.getChosenOutputPortName();
                if (dataCentre.openSelectedDCCOutputPort())
                {

                    confirmationError2.setText(port + " is opened for DCC connection.");
                }
                else
                {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Cannot connect to DCC connection");
                    a.show();
                    //confirmationError.setText("Cannot connect to" + port + " for DCC connection.");
                }

            }
        });
    }
    /**
     * A method to create the bottom half of the GUI.
     */
    private void makeBottom()
    {
        confirmationError = new Text();
        inputText = new Text("Input from Arduino");
        input = new TextArea("Start");
        input.setEditable(false);
        USBOutputDCCChooser = new ComboBox();
        makeUSBOutputDCCChooser();
        outputText = new Text("Output from this program");
        output = new TextArea();
        output.setEditable(false);
        trainText = new Text("Trains that are loaded");
        trainOutput = new TextArea();
        trainOutput.setEditable(false);
        sendDCCLabel = new Label("Type train DCC commands : ");
        confirmationError2 = new Text();
        sendDCCHBox = new HBox();
        sendDCCTextField = new TextField();
        sendDCCTextField.setPrefWidth(600);
        sendDCCButton = new Button("Send");
        sendDCCButton.setMinWidth(40);
        sendDCCButton.setOnAction(actionEvent -> handleSendDCCCommand(sendDCCTextField.getText()));
        sendDCCHBox.getChildren().add(sendDCCTextField);

        sendDCCHBox.getChildren().add(sendDCCButton);
        powerDCCButton = new Button("On");
        powerDCCButton.setOnAction(actionEvent -> handleTogglePower());
        layout = new VBox();
    }

    /**
     * A method to add all children into the JavaFX Scene graph.
     */
    private void addChildren()
    {
        layout.getChildren().add(title);
        layout.getChildren().add(addTrain);
        layout.getChildren().add(removeTrain);
        layout.getChildren().add(USBLayout);
        layout.getChildren().add(WIFIMode);
        layout.getChildren().add(parseLayout);
        layout.getChildren().add(startStop);
        layout.getChildren().add(confirmationError);
        layout.getChildren().add(inputText);
        layout.getChildren().add(input);
        layout.getChildren().add(USBOutputDCCChooser);
        layout.getChildren().add(outputText);
        layout.getChildren().add(output);
        layout.getChildren().add(trainText);
        layout.getChildren().add(trainOutput);
        layout.getChildren().add(sendDCCLabel);
        layout.getChildren().add(sendDCCHBox);
        layout.getChildren().add(powerDCCButton);
        layout.getChildren().add(confirmationError2);
    }

    /**
     * A method to return the GUI.
     * @return A VBOX  with all GUI elements.
     */
    public VBox getGUI()
    {
        return layout;
    }

}
