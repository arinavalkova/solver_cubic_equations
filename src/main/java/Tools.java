import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Tools
{
    public static Parent loadFromFXML(String nameOfFXML)
    {
        Parent parent = null;

        try
        {
            parent = FXMLLoader.load(Tools.class.getResource(nameOfFXML));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return parent;
    }
}
