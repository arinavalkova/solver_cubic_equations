import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainApplicationController
{
    private static final double MAX_VALUE = 0x7fffffff;
    private ArrayList<Double> coefficients;

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
    private Label rootsLabel;

    @FXML
    private Label countOfRootsLabel;

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
            cleanLabels();

            coefficients = getCoefficients();
            SolverCubicEquations solverCubicEquations = new SolverCubicEquations(coefficients);

            ArrayList<Double> rootsOfEquation = solverCubicEquations.solveEquation();
            if (rootsOfEquation != null)
            {
                printRoots(rootsOfEquation);
            }

            solvePrintGraphic(rootsOfEquation);

        } catch (NumberFormatException exception)
        {
            errorsLabel.setText("Error: Bad input!");
        }
    }

    private void printRoots(ArrayList<Double> rootsOfEquation)
    {
        int countOfRoots = 0;
        String rootsLine = "";

        for(Double currentRoot : rootsOfEquation)
        {
            countOfRoots++;
            DecimalFormat df = new DecimalFormat("#.####");
            rootsLine += df.format(currentRoot) + " ";
        }

        rootsLabel.setText(rootsLine);
        countOfRootsLabel.setText("Count of roots: " + countOfRoots);
    }

    private void solvePrintGraphic(ArrayList<Double> rootsOfEquation)
    {
        double min = MAX_VALUE, max = 0;

        if(rootsOfEquation.size() > 1)
        {
            for(double currentRoot : rootsOfEquation)
            {
                if(currentRoot < min)
                    min = currentRoot;
                if(currentRoot > max)
                    max = currentRoot;
            }
        }

        if(rootsOfEquation.size() == 1)
        {
            printGraphicWithOneRoot(rootsOfEquation.get(0));
        }
        else if(rootsOfEquation.size() == 2 || rootsOfEquation.size() == 3)
        {
            printGraphicWithMultipleRoots(min, max);
        }
        else
        {
            System.out.println("Bad count of roots!");
        }
    }

    private void printGraphicWithOneRoot(double root)
    {
        printGraphic((int) root - 5, (int) root + 5, 1);
    }

    private void printGraphicWithMultipleRoots(double minRoot, double maxRoot)
    {
        int step;
        if((int)((maxRoot - minRoot) / 10) == 0)
        {
            step = 1;
        }
        else
        {
            step = (int) ((maxRoot - minRoot) / 10);
        }
        printGraphic((int) minRoot - 3 * step, (int) maxRoot + 3 * step, step);
    }

    private void printGraphic(int leftScope, int rightScope, int step)
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName(coefficients.get(0) + " * x^3 + " + coefficients.get(1) + " * x^2 + " + coefficients.get(2) +
                " * x + " + coefficients.get(3));

        for(int i = leftScope; i <= rightScope; i += step)//вычислить тут с какого i лучше по какое и шаг
        {
            series.getData().add(new XYChart.Data(i, SolverCubicEquations.cubicEquation(i)));
        }

        if(!graphicsPane.getChildren().isEmpty())
            graphicsPane.getChildren().clear();
        graphicsPane.getChildren().add(lineChart);
        lineChart.getData().add(series);
    }

    private void cleanLabels()
    {
        errorsLabel.setText("");
        rootsLabel.setText("");;
        countOfRootsLabel.setText("");
    }

    private ArrayList<Double> getCoefficients()
    {
        ArrayList<Double> coefficients = new ArrayList<>();

        coefficients.add(Double.parseDouble(firstTextField.getText()));
        coefficients.add(Double.parseDouble(secondTextField.getText()));
        coefficients.add(Double.parseDouble(thirdTextField.getText()));
        coefficients.add(Double.parseDouble(fourthTextField.getText()));

        return coefficients;
    }
}
