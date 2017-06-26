import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import arduino.*;

/*
    extend Slider class to avoid setting the same
    properties 4 times to each slider we create
    (better should be in separated class but never mind)
 */
class ServoSlider extends Slider{
    public ServoSlider(){
        setMin(0);
        setMax(180);
        setValue(90);
        setShowTickLabels(true);
        setShowTickMarks(true);
        setMajorTickUnit(50);
        setMinorTickCount(5);
        setBlockIncrement(5);
    }
}

public class Main extends Application {
    /*
        well, here we declare graphical controls
        the reason why im declaring some components as array is it's shorter
        than declaring each one in separated line
     */
    private ServoSlider[] servoSliders = new ServoSlider[4];
    private Label[] servoLabels = new Label[4];
    private TextField[] degInput = new TextField[4];
    // im using 4 servos, adruino selecting them with letters a - d
    private Character[] identifiers = {'a', 'b', 'c', 'd'};
    private Button resetBut;

    // now find the active port, to do so we use separated class
    private static String port = (new SPortScan()).getActivePort();
    /*
        now tell the selected port to the constructor of our libary
        second argument is baud rate that should be the same as in adruino program
     */
    private static Arduino AdruinoCon = new Arduino(port, 9600);

    /*
        JavaFX method that run the main logic of windowed program
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Serial Servo Controller"); // title of window
        /*
            when "X" is clicked to close the program, we want to do some clean up,
            in my case set the servos back to default position
         */
        primaryStage.setOnCloseRequest(e -> {
            cleanUp();
        });

        // init the button and by lambda function make it perform the function if it has been clicked
        resetBut = new Button("Reset angle");
        resetBut.setOnAction(e -> {
            resetAngle();
        });

        /*
            now maybe most confusing part
            since i created my components in array, im now able to iterate that array
            that lets me write code once and apply it to all 4 components
         */

        for (int i = 0; i < servoSliders.length; i++){
            final int c = i; // constant to use it in inner functions

            /*
                inicialization of components
             */
            servoLabels[c] = new Label("Servo #" + i);
            servoSliders[c] = new ServoSlider();
            degInput[c] = new TextField(Integer.toString( (int) servoSliders[i].getValue()));
            degInput[c].setPrefWidth(50);

            /*
                make theinput change our slider value every time it's value has been changed
            */
            degInput[c].textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                    // with some regex check if its number, else set back old value
                    if(newValue.matches("^-?\\d+$")) {
                        if (Integer.parseInt(newValue) > 0 && Integer.parseInt(newValue) <= 180) {
                            servoSliders[c].setValue(Integer.parseInt(newValue));
                        }
                    }else{
                        degInput[c].setText(oldValue);
                    }
                }
            });

            /*
                most important part, here we sending data to adruino
                whenever is slider dragged, or it's value is changed by inputs we send that value to adruino
             */
            servoSliders[c].valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    int position = (int) Math.round(servoSliders[c].getValue());
                    /*
                        here is the magic, we take the value of slider and appropriate character from identifier array
                        to tell the adruino which servo we want to move and send it to USB
                     */
                    AdruinoCon.serialWrite( position + Character.toString(identifiers[c]));
                    degInput[c].setText(Integer.toString(position)); // set input to show right value
                }
            });
        }


        // init layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(8);
        layout.setHgap(10);

        /*
            since my components is in array i could add them to the layout in for loop
         */
        for(int i = 0; i < servoSliders.length; i++) {

            /*
                Im using gridpane here and it uses rows and columns to set the position of comps
                well, every servo have its label, slider and input to set value
                i want those controls in one row of each servo
                to do so i set every component diferent column and row by variable
             */
            layout.setConstraints(servoLabels[i], 1, i);
            layout.setConstraints(servoSliders[i], 2, i);
            layout.setConstraints(degInput[i], 3, i);

            layout.setHgrow(servoSliders[i], Priority.ALWAYS); // make slider fill the row

            // add components to layout
            layout.getChildren().add(servoLabels[i]);
            layout.getChildren().add(servoSliders[i]);
            layout.getChildren().add(degInput[i]);
        }

        // add reset button in the end
        layout.setConstraints(resetBut, 1,servoSliders.length + 1);
        layout.getChildren().add(resetBut);

        // create scene, its Java FX jargon
        Scene scene = new Scene(layout, 500, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cleanUp() {
        resetAngle();
        AdruinoCon.closeConnection();
    };

    private void resetAngle(){
        for(ServoSlider slider : servoSliders){
            slider.setValue(90);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        AdruinoCon.openConnection();
        /*
            whenever connection is estabilished the adruino is restarted, we need to wait for it
            otherwise it wont listen
         */
        Thread.sleep(4000);
        launch(args);
    }
}
