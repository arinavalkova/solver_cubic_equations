import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MainApplicationController
{
    private static double a;
    private static double b;
    private static double c;
    private static double d;

    @FXML
    private JFXTextField firstTextField;

    @FXML
    private JFXTextField secondTextField;

    @FXML
    private JFXTextField thirdTextField;

    @FXML
    private JFXTextField fourthTextField;

    @FXML
    private Label errorsLabel;

    @FXML
    private JFXButton solveButton;

    @FXML
    private AnchorPane graphicsPane;

    @FXML
    void initialize()
    {
        setEvents();
    }

    private void setEvents()
    {
        setSolveButtonEvent();
    }

    private void setSolveButtonEvent()
    {
        solveButton.setOnAction(event ->
        {
            startSolving();
        });
    }

    private void startSolving()
    {
        try
        {
            cleanErrorLabel();
            getCoefficients();
            solveEquation();
            printGraphic();

        } catch (NumberFormatException exception)
        {
            errorsLabel.setText("Error: Bad input!");
        }
    }

    private void cleanErrorLabel()
    {
        errorsLabel.setText("");
    }

    private void getCoefficients()
    {
        a = Double.parseDouble(firstTextField.getText());
        b = Double.parseDouble(secondTextField.getText());
        c = Double.parseDouble(thirdTextField.getText());
        d = Double.parseDouble(fourthTextField.getText());
    }

    private void printGraphic()
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        XYChart.Series series = new XYChart.Series();

        for(int i = -10; i <= 10; i += 1)//вычислить тут с какого i лучше по какое и шаг
        {
            series.getData().add(new XYChart.Data(i, cubicEquation(i)));
        }

        if(!graphicsPane.getChildren().isEmpty())
            graphicsPane.getChildren().clear();
        graphicsPane.getChildren().add(lineChart);
        lineChart.getData().add(series);
    }

    private double cubicEquation(double i)
    {
        return a * i * i * i + b * i * i + c * i + d;
    }

    private void solveEquation()
    {
        System.out.println("Coefficients: " + a + " " + b + " " + c + " " + d);
    }
}
