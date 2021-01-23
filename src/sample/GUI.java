package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
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
    private ComboBox USBChooser;
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
    private Text outputText;
    private TextArea output;
    private VBox layout;

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
     * A method to create the top half of the GUI.
     */
    private void makeTop()
    {

        title = new Text("Train control");
        addTrain = new Button("Add Train");
        addTrain.setOnAction(actionEvent ->  {
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


            Label maxDecelerationLabel = new Label("train max deceleration: ");
            TextField maxDecelerationTextField = new TextField();
            GridPane.setConstraints(maxDecelerationLabel, 0, 3);
            newTrainFormLayout.getChildren().add(maxDecelerationLabel);
            GridPane.setConstraints(maxDecelerationTextField, 1, 3);
            newTrainFormLayout.getChildren().add(maxDecelerationTextField);


            Label maxAccelerationLabel = new Label("train max Acceleration: ");
            TextField maxAccelerationTextField = new TextField();
            GridPane.setConstraints(maxAccelerationLabel, 0, 4);
            newTrainFormLayout.getChildren().add(maxAccelerationLabel);
            GridPane.setConstraints(maxAccelerationTextField, 1, 4);
            newTrainFormLayout.getChildren().add(maxAccelerationTextField);


            Label topSpeedLabel = new Label("train top speed: ");
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
                if(!idTextField.getText().matches("\\d{3}"))
                {
                    idTextField.setText("");
                    errorLabel.setText("Needs to be 3 digits integer, with leading zeroes. e.g.(001,010,100)");
                    errorLabel.setVisible(true);
                }
                else
                {
                    errorLabel.setVisible(false);
                }
            });


            Scene popUpAddTrainScene = new Scene(newTrainFormLayout,600,400);
            addTrainStage.setScene(popUpAddTrainScene);
            if (!addTrainStage.isShowing())
            {
                addTrainStage.show();
            }

        });
        removeTrain = new Button("Remove Train");
        removeTrain.setVisible(false);


        USBLayout = new HBox();
        USBMode = new RadioButton("USB mode");
        USBMode.setToggleGroup(USBWIFIGroup);
        USBMode.setUserData("USB");
        USBChooser = new ComboBox();
        USBChooser.setDisable(true);
        USBChooser.setVisible(false);
        USBLayout.getChildren().add(USBMode);
        USBLayout.getChildren().add(USBChooser);
        WIFIMode = new RadioButton("WIFI mode");
        WIFIMode.setToggleGroup(USBWIFIGroup);
        WIFIMode.setUserData("WIFI");

        USBWIFIGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {
                if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("USB"))
                {
                    //Create ComboBox with options of COM ports.
                    USBChooser.setItems(FXCollections
                            .observableArrayList(dataCentre.getComNameList()));
                    USBChooser.getSelectionModel().selectFirst();
                    dataCentre.setChosenPort(USBChooser.getValue().toString());
                    dataCentre.setPortFromPortName(USBChooser.getValue().toString());
                    USBChooser.setDisable(false);
                    USBChooser.setVisible(true);

                }
                if (USBWIFIGroup.getSelectedToggle().getUserData().toString().equals("WIFI"))
                {
                    USBChooser.setDisable(true);
                    USBChooser.setVisible(false);
                    dataCentre.setChosenPort("");
                    dataCentre.setCurrentPort(null);
                }

            }
        });
        parseLayout = new HBox();
        UIDParse = new RadioButton("UID mode");
        newDataParse = new RadioButton("New Data mode");
        UIDParse.setToggleGroup(dataGroup);
        newDataParse.setToggleGroup(dataGroup);
        dataGroup.selectToggle(newDataParse);
        dataCentre.setStandardParse(true);
        parseLayout.getChildren().add(UIDParse);
        parseLayout.getChildren().add(newDataParse);
        dataGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
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
        });

        USBChooser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldvalue, final String newvalue)
            {
                dataCentre.setChosenPort(newvalue);
                dataCentre.setPortFromPortName(newvalue);
            }
        });


        startStop = new Button("Start");
        startStop.setOnAction(actionEvent ->  {
            if (startStop.getText().equals("Start"))
            {
                startStop.setText("Stop");
                String port = dataCentre.getChosenPort();
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
                                if (dataCentre.openSelectedPort())
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
                    if (dataCentre.closeSelectedPort())
                    {
                        confirmationError.setText("Connection to" + dataCentre.getChosenPort() + " is closed");
                    }
                }
                else
                {

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
        outputText = new Text("Output from this program");
        output = new TextArea();
        output.setEditable(false);
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
        layout.getChildren().add(outputText);
        layout.getChildren().add(output);
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
