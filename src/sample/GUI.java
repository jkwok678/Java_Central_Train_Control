package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.util.Timer;
import java.util.TimerTask;

public class GUI {

    private Text title;
    private RadioButton USBMode;

    private RadioButton WIFIMode;
    private Button startStop;
    boolean go;
    private final ToggleGroup onlyGroup = new ToggleGroup();
    private DataCentre dataCentre;
    private Text inputText;
    private TextArea input;
    private Text outputText;
    private TextArea output;
    private VBox layout;

    public GUI(DataCentre dataEntry)
    {
        dataCentre = dataEntry;
        title = new Text("Train control");

        USBMode = new RadioButton("USB mode");
        USBMode.setToggleGroup(onlyGroup);
        USBMode.setUserData("USB");

        WIFIMode = new RadioButton("WIFI mode");
        WIFIMode.setToggleGroup(onlyGroup);
        WIFIMode.setUserData("WIFI");

        startStop = new Button("Start");
        startStop.setOnAction(actionEvent ->  {
            if (startStop.getText().equals("Start"))
            {
                startStop.setText("Stop");
                go = true;
            }
            else
            {
                startStop.setText("Start");
                go = false;
            }

        });

        inputText = new Text("Input from Arduino");
        input = new TextArea("Start");
        input.setEditable(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(go)
                {
                    String current = input.getText();
                    //input.setText(current + "\n" + dataCentre.readData());
                }

            }
        }, 0, 100);
        outputText = new Text("Output from this program");
        output = new TextArea();
        output.setEditable(false);
        layout = new VBox();
        addChildren();
    }

    private void addChildren()
    {
        layout.getChildren().add(title);
        layout.getChildren().add(USBMode);
        layout.getChildren().add(WIFIMode);
        layout.getChildren().add(startStop);
        layout.getChildren().add(inputText);
        layout.getChildren().add(input);
        layout.getChildren().add(outputText);
        layout.getChildren().add(output);
    }

    public VBox getGUI()
    {
        return layout;
    }



}
