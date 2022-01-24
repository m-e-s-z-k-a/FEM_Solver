import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WelcomeScreen extends VBox {

    Label title = new Label ("FEM Solver");
    TextField n_textfield = new TextField();
    Text n_text = new Text("n = ");
    HBox n_box = new HBox(n_text, n_textfield);
    Button button = new Button("Start!");
    public WelcomeScreen()
    {
        super();
        title.setAlignment(Pos.CENTER);
        n_text.setFont(new Font("Comic Sans MS", 15));
        title.setFont(new Font("Comic Sans MS", 20));
        button.setFont(new Font("Comic Sans MS", 15));
        n_box.setAlignment(Pos.CENTER);
        button.setAlignment(Pos.CENTER);
        n_box.setSpacing(15);
        VBox welcomescreen_vbox = new VBox(title, n_box, button);
        welcomescreen_vbox.setSpacing(20);
        welcomescreen_vbox.setAlignment(Pos.CENTER);
        this.getChildren().add(welcomescreen_vbox);
        this.setAlignment(Pos.CENTER);
    }


    public int getN(){
        int n = Integer.parseInt(n_textfield.getText());
        if(n < 0){
            throw new IllegalArgumentException("n can't be less than 0");
        }
        return n;
    }
}
