import java.util.ArrayList;

public class SolverCubicEquations
{
    private static double a;
    private static double b;
    private static double c;
    private static double d;

    private static final double EPSILON = 0.0001;

    public SolverCubicEquations(ArrayList<Double> coefficients)
    {
        a = coefficients.get(0);
        b = coefficients.get(1);
        c = coefficients.get(2);
        d = coefficients.get(3);
    }

    public ArrayList<Double> solveEquation()
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

    public static double cubicEquation(double i)
    {
        return a * i * i * i + b * i * i + c * i + d;
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
            System.out.println("Unknown way!");
        }

        return rootsOfEquation;
    }
}
