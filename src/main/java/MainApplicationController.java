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

            solvePrintGraphic(rootsOfEquation);

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

    private void solvePrintGraphic(ArrayList<Double> rootsOfEquation)
    {
        double min = 9999999, max = 0;///////////////////

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
        System.out.println(step);
        printGraphic((int) minRoot - 3 * step, (int) maxRoot + 3 * step, step);
    }

    private void printGraphic(int leftScope, int rightScope, int step)
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName(a + " * x^3 + " + b + " * x^2 + " + c + " * x + " + d);

        for(int i = leftScope; i <= rightScope; i += step)//вычислить тут с какого i лучше по какое и шаг
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
            rootsOfEquation.add(searchingLeftScope(0, a > 0));
        }
        else
        {
            rootsOfEquation.add(searchingRightScope(0, a > 0));
        }
        return rootsOfEquation;
    }

    private double searchingLeftScope(double scope, boolean growUp)
    {
        double leftScope = 0, rightScope = scope, currentValue = scope;

        if(a > 0)
        {
            while (cubicEquation(currentValue) >= 0)
            {
                rightScope = currentValue;
                currentValue -= 1;
            }
        }
        else
        {
            while (cubicEquation(currentValue) <= 0)
            {
                rightScope = currentValue;
                currentValue -= 1;
            }
        }

        leftScope = currentValue;

        return searchingValueInScope(leftScope, rightScope, growUp);
    }

    private double searchingValueInScope(double leftScope, double rightScope, boolean growUp)
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

        if(growUp && currentValueOfEquation > 0 || !growUp && currentValueOfEquation < EPSILON)
            return searchingValueInScope(leftScope, mid, growUp);
        else
            return searchingValueInScope(mid, rightScope, growUp);
    }

    private double searchingRightScope(double scope, boolean growUp)
    {
        double leftScope = scope, rightScope, currentValue = scope;

        if(a > 0)
        {
            while (cubicEquation(currentValue) <= 0)
            {
                leftScope = currentValue;
                currentValue += 1;
            }
        }
        else
        {
            while (cubicEquation(currentValue) >= 0)
            {
                leftScope = currentValue;
                currentValue += 1;
            }
        }

        rightScope = currentValue;

        return searchingValueInScope(leftScope, rightScope, growUp);
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
                rootsOfEquation.add(searchingRightScope(Math.min(firstRoot, secondRoot), a > 0));
            }
            else
            {
                rootsOfEquation.add(searchingLeftScope(Math.max(firstRoot, secondRoot), a > 0));
            }
        }
        else if(cubicEquation(firstRoot) > EPSILON && cubicEquation(secondRoot) > EPSILON)
        {
            if(a > 0)
            {
                rootsOfEquation.add(searchingLeftScope(Math.min(firstRoot, secondRoot), a > 0));
            }
            else
            {
                rootsOfEquation.add(searchingRightScope(Math.max(firstRoot, secondRoot), a > 0));
            }
        }
        else if(Math.abs(cubicEquation(Math.min(firstRoot, secondRoot))) < EPSILON)
        {
            double root1 = Math.min(firstRoot, secondRoot);
            rootsOfEquation.add(root1);

            double root2 = searchingRightScope(Math.min(firstRoot, secondRoot), a > 0);
            if(root2 != root1)
            {
                rootsOfEquation.add(root2);
            }
        }
        else if(Math.abs(cubicEquation(Math.max(firstRoot, secondRoot))) < EPSILON)
        {
            double root1 = Math.max(firstRoot, secondRoot);
            rootsOfEquation.add(root1);

            double root2 = searchingLeftScope(Math.max(firstRoot, secondRoot), a > 0);
            if(root2 != root1)
            {
                rootsOfEquation.add(root2);
            }
        }
        else if(cubicEquation(firstRoot) > EPSILON && cubicEquation(secondRoot) < - EPSILON ||
                cubicEquation(firstRoot) < -EPSILON && cubicEquation(secondRoot) > EPSILON)
        {
            rootsOfEquation.add(searchingLeftScope(Math.min(firstRoot, secondRoot), a > 0));
            rootsOfEquation.add(searchingValueInScope(Math.min(firstRoot, secondRoot), Math.max(firstRoot, secondRoot), a < 0));
            rootsOfEquation.add(searchingRightScope(Math.max(firstRoot, secondRoot), a > 0));
        }
        else
        {
            rootsLabel.setText("Unknown way!");
        }

        return rootsOfEquation;
    }
}
