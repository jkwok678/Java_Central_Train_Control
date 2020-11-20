package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                if (dataCentre.openSelectedPort())
                {

                    confirmationError.setText(port + " is opened.");
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            if (startStop.getText().equals("Stop"))
                            {
                                //Read from USB
                                dataCentre.readDataFromUSB();
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
                    confirmationError.setText("Cannot connect to" + port);
                }
            }
            else
            {
                startStop.setText("Start");
                go = false;
                if (dataCentre.closeSelectedPort())
                {
                    confirmationError.setText("Connection to" + dataCentre.getChosenPort() + " is closed");
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
