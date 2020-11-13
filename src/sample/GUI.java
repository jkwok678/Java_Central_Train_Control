package sample;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GUI {

    Text title;
    RadioButton USBMode;
    RadioButton WIFIMode;
    final ToggleGroup onlyGroup = new ToggleGroup();
    Text inputText;
    TextArea input;
    Text outputText;
    TextArea output;
    VBox layout;

    public GUI()
    {
         title = new Text("Train control");
         USBMode = new RadioButton("USB mode");
         USBMode.setToggleGroup(onlyGroup);
         WIFIMode = new RadioButton("WIFI mode");
         WIFIMode.setToggleGroup(onlyGroup);
         inputText = new Text("Input from Arduino");
         input = new TextArea();
         input.setEditable(false);
         outputText = new Text("Output from this program");
         output = new TextArea();
         output.setEditable(false);
         layout = new VBox();
    }

    public VBox getGUI()
    {

        layout.getChildren().add(title);
        layout.getChildren().add(USBMode);
        layout.getChildren().add(WIFIMode);
        layout.getChildren().add(inputText);
        layout.getChildren().add(input);
        layout.getChildren().add(outputText);
        layout.getChildren().add(output);


        return layout;
    }
}
