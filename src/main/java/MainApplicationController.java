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
    private static double a;
    private static double b;
    private static double c;
    private static double d;

    private static final double EPSILON = 0.0001;

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
            getCoefficients();

            ArrayList<Double> rootsOfEquation = solveEquation();
            if (rootsOfEquation != null)
            {
                printRoots(rootsOfEquation);
            }

            printGraphic(rootsOfEquation);

        } catch (NumberFormatException exception)
        {
            errorsLabel.setText("Error: Bad input!");
        }
    }

    private void cleanLabels()
    {
        errorsLabel.setText("");
        rootsLabel.setText("");;
        countOfRootsLabel.setText("");
    }

    private void getCoefficients()
    {
        a = Double.parseDouble(firstTextField.getText());
        b = Double.parseDouble(secondTextField.getText());
        c = Double.parseDouble(thirdTextField.getText());
        d = Double.parseDouble(fourthTextField.getText());
    }

    private void printGraphic(ArrayList<Double> rootsOfEquation)
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName(a + " * x^3 + " + b + " * x^2 + " + c + " * x + " + d);

        for(int i = -4; i <= 4; i += 1)//вычислить тут с какого i лучше по какое и шаг
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

    private ArrayList<Double> solveEquation()
    {
        ArrayList<Double> rootsOfEquation = null;

        if(isDiscriminantMoreThanZero())
        {
            rootsOfEquation = oneRootSearching();
        }
        else
        {
            rootsOfEquation = multipleRootsSearching();
        }
        return rootsOfEquation;
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

        rootsLabel.setText(String.valueOf(rootsLine));
        countOfRootsLabel.setText("Count of roots: " + countOfRoots);
    }

    private boolean isDiscriminantMoreThanZero()
    {
        return 4 * b * b - 12 * a * c < 0;
    }

    private ArrayList<Double> oneRootSearching()
    {
        ArrayList<Double> rootsOfEquation = new ArrayList<>();

        if(a > 0 && cubicEquation(0) > 0 || a < 0 && cubicEquation(0) < 0)
        {
            rootsOfEquation.add(searchingLeftScope(0));
        }
        else
        {
            rootsOfEquation.add(searchingRightScope(0));
        }
        return rootsOfEquation;
    }

    private double searchingLeftScope(double scope)
    {
        double leftScope, rightScope = scope, currentValue = 0;

        if(a > 0)
        {
            while (cubicEquation(currentValue) > 0)
            {
                rightScope = currentValue;
                currentValue -= 1;
            }
        }
        else
        {
            while (cubicEquation(currentValue) < 0)
            {
                rightScope = currentValue;
                currentValue -= 1;
            }
        }

        leftScope = currentValue;

        return searchingValueInScope(leftScope, rightScope);
    }

    private double searchingValueInScope(double leftScope, double rightScope)
    {
        double currentValueOfEquation = cubicEquation(leftScope);
        if(Math.abs(currentValueOfEquation) <= EPSILON)
            return leftScope;

        currentValueOfEquation = cubicEquation(rightScope);
        if(Math.abs(currentValueOfEquation) <= EPSILON)
            return rightScope;

        double mid = (double)leftScope / 2 + (double)rightScope / 2;
        currentValueOfEquation = cubicEquation(mid);
        if(Math.abs(currentValueOfEquation) <= EPSILON)
            return mid;

        if(a > 0 && currentValueOfEquation > 0 || a < 0 && currentValueOfEquation < EPSILON)
            return searchingValueInScope(leftScope, mid);
        else
            return searchingValueInScope(mid, rightScope);
    }

    private double searchingRightScope(double scope)
    {
        double leftScope = scope, rightScope, currentValue = 0;

        if(a > 0)
        {
            while (cubicEquation(currentValue) < 0)
            {
                leftScope = currentValue;
                currentValue += 1;
            }
        }
        else
        {
            while (cubicEquation(currentValue) > 0)
            {
                leftScope = currentValue;
                currentValue += 1;
            }
        }

        rightScope = currentValue;

        return searchingValueInScope(leftScope, rightScope);
    }

    private ArrayList<Double> multipleRootsSearching()
    {
        ArrayList<Double> rootsOfEquation = new ArrayList<>();

        double firstRoot = (double)(-2 * b) / (6 * a) + (double)Math.sqrt(4 * b * b - 12 * a * c) / (6 * a);
        double secondRoot = (double)(-2 * b) / (6 * a) - (double)Math.sqrt(4 * b * b - 12 * a * c) / (6 * a);

        if(cubicEquation(firstRoot) < -EPSILON && cubicEquation(secondRoot) < -EPSILON)
        {
            if(a > 0)
            {
                rootsOfEquation.add(searchingRightScope(Math.min(firstRoot, secondRoot)));
            }
            else
            {
                rootsOfEquation.add(searchingLeftScope(Math.max(firstRoot, secondRoot)));
            }
        }
        else if(cubicEquation(firstRoot) > EPSILON && cubicEquation(secondRoot) > EPSILON)
        {
            if(a > 0)
            {
                rootsOfEquation.add(searchingLeftScope(Math.min(firstRoot, secondRoot)));
            }
            else
            {
                rootsOfEquation.add(searchingRightScope(Math.max(firstRoot, secondRoot)));
            }
        }
        else if(Math.abs(cubicEquation(Math.min(firstRoot, secondRoot))) < EPSILON)
        {
            double root1 = Math.min(firstRoot, secondRoot);
            rootsOfEquation.add(root1);

            double root2 = searchingRightScope(Math.min(firstRoot, secondRoot));
            if(root2 != root1)
            {
                rootsOfEquation.add(root2);
            }
        }
        else if(Math.abs(cubicEquation(Math.max(firstRoot, secondRoot))) < EPSILON)
        {
            double root1 = Math.max(firstRoot, secondRoot);
            rootsOfEquation.add(root1);

            double root2 = searchingLeftScope(Math.max(firstRoot, secondRoot));
            if(root2 != root1)
            {
                rootsOfEquation.add(root2);
            }
        }

        return rootsOfEquation;
    }
}
