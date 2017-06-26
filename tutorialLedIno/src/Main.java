import arduino.Arduino;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private Button ledSwitch; // control button
    // You can check com port in adruino IDE and write it directly as string i.e. "COM5"
    private static String port = (new SPortScan()).getActivePort();
    // create connection to adruino
    private static Arduino AdruinoCon = new Arduino(port, 9600);

    private boolean isOn = false; // state of the led
    String[] commands = {"turnOff", "turnOn"}; // commands that adruino can recognize

    // JavaFX main function containing the logic
    @Override
    public void start(Stage primaryStage) throws Exception{
        // this method is called when window is closed, lets make sure we closed the connection
        primaryStage.setOnCloseRequest(e -> {
            AdruinoCon.closeConnection();
        });

        // init the button
        ledSwitch = new Button("Turn On/Off");

        // this will be caled whenever we click the button
        ledSwitch.setOnAction(e -> {
            this.isOn = !this.isOn; // if the current state is TRUE we set it to FALSE
            int commandIndex = (this.isOn) ? 1 : 0; // false = 0; true = 1
            AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
        });

        /*
            Here is some JavaFX code, basicaly we need layout to set the position
            of the components, since we have only one, the simplest layout is good enough
          */
        StackPane layout = new StackPane();
        layout.getChildren().add(ledSwitch);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
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
