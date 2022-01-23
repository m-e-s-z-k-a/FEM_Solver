public class FEMSolver {

    public static int n;
    public EquationChart equation_chart = new EquationChart("x","u(x)");

    public FEMSolver(int n)
    {
        FEMSolver.n = n;
    }

    public void start()
    {
        double[] matrixL = new double[n];
        double[][] matrixB = new double[n][n];
        for (int i = 0; i<n; i++)
        {
            for(int j = 0; j<n; j++)
            {
                matrixB[i][j] = getMatrixB(i, j);
            }
        }
        for(int i=0;i<n;i++){
            matrixL[i] = getMatrixL(i);
        }
        double[] result = gaussianElimination(matrixB,matrixL);
        drawChart(result);

    }

    //liczenie wartosci xi dla konkretnego i
    public float getXi(int i)
    {
        return (float) (3*i)/ (float) FEMSolver.n;
    }

    //szukanie poczatku 1-szej czesci Ei
    public float getEiLow(int i, int number)
    {
        if (number == 0 && i - 1>= 0)
        {
            return getXi(i-1);
        }
        if (i <= FEMSolver.n)
        {
            return getXi(i);
        }
        return Float.NEGATIVE_INFINITY;
    }

    //szukanie poczatku 2-giej czesci Ei
    public float getEiHigh(int i, int number)
    {
        if (number == 0)
        {
            return getXi(i);
        }
        if (i + 1 <= FEMSolver.n)
        {
            return getXi(i+1);
        }
        return Float.NEGATIVE_INFINITY;
    }

    //wyliczanie ei(x)
    public double getEi(int i, double x)
    {
        if (x >= getXi(i-1) && x <= getXi(i) && i-1>=0)
        {
            return (x-getXi(i-1))/(getXi(i)-getXi(i-1));
        }
        else if(x >= getXi(i) && x <= getXi(i+1))
        {
            return (getXi(i+1)-x)/(getXi(i+1)-getXi(i));
        }
        else
        {
            return 0;
        }
    }


    //pochodna z ei(x)
    public double getEiDerivative(int i, double x)
    {
        if (x >= getXi(i-1) && x <= getXi(i) && i-1>=0)
        {
            return (float) 1/(getXi(i)-getXi(i-1));
        }
        else if(x >= getXi(i) && x <= getXi(i+1)){
            return (float) (-1)/(getXi(i+1)-getXi(i));
        }
        else{
            return 0;
        }
    }

    //szukanie przedizalow w ktorych przecinaja sie ei, ej zeby obliczyc calke
    public float getIntegral(int i, int j)
    {
        float result = 0;
        result += gaussianQuadrature(i, j, Math.max(getEiLow(i, 0), getEiLow(j, 0)), Math.min(getEiHigh(i, 0), getEiHigh(j, 0)));
        result += gaussianQuadrature(i, j, Math.max(getEiLow(i, 0), getEiLow(j, 1)), Math.min(getEiHigh(i, 0), getEiHigh(j, 1)));
        result += gaussianQuadrature(i, j, Math.max(getEiLow(i, 1), getEiLow(j, 0)), Math.min(getEiHigh(i, 1), getEiHigh(j, 0)));
        result += gaussianQuadrature(i, j, Math.max(getEiLow(i, 1), getEiLow(j, 1)), Math.min(getEiHigh(i, 1), getEiHigh(j, 1)));
        return result;
    }

    public double getMatrixB(int i, int j)
    {
        return getEi(i,0)*getEi(j, 0) - getIntegral(i, j);
    }

    public double getMatrixL(int i)
    {
        return 5*getEi(i, 0) - gaussianQuadrature2(i) - getMatrixB(n, i); //czy to jest ok?? nie wiem
    }


    //liczenie calki potrzebnej do wyznaczenia L(ei) - metoda Gaussa-Legendre'a
    public double gaussianQuadrature2(int i)
    {
        double[] points = { -0.774597, 0,  0.774597};
        double[] weights = { 0.555556, 0.888889, 0.555556};
        double result = 0;
        double x1 = getXi(i-1);
        double x2 = getXi(i+1);
        if(x1<x2 && x1 != Double.NEGATIVE_INFINITY)
        {
            for (int index = 0; index <3; index ++)
            {
                if (x1 < 1 && x2 <= 1)
                {
                    result += 0.1*weights[index]*getEi(i,(0.5*(x2-x1)*points[index] + (x2+x1)*0.5));
                }
                else if (x1 < 1 && x2 <= 2)
                {
                    result += 0.1*weights[index]*getEi(i, (0.5*(1-x1)*points[index] + (1+x1)*0.5));
                    result += 0.2*weights[index]*getEi(i, (0.5*(x2-1)*points[index] + (1+x2)*0.5));
                }
                else if (x1 < 1 && x2 <= 3)
                {
                    result += 0.1*weights[index]*getEi(i, (0.5*(1-x1)*points[index] + (1+x1)*0.5));
                    result += 0.2*weights[index]*getEi(i, (0.5*(2-1)*points[index] + (2+1)*0.5));
                    result += weights[index]*getEi(i, (0.5*(x2-2)*points[index] + (2+x2)*0.5));
                }
                else if (x1 < 2 && x2 <= 2)
                {
                    result += 0.2*weights[index]*getEi(i,(0.5*(x2-x1)*points[index] + (x2+x1)*0.5));
                }
                else if (x1 < 2 && x2 <= 3)
                {
                    result += 0.2*weights[index]*getEi(i, (0.5*(2-x1)*points[index] + (2+x1)*0.5));
                    result += weights[index]*getEi(i, (0.5*(x2-2)*points[index] + (2+x2)*0.5));
                }
                else if (x1 < 3 && x2 <= 3)
                {
                    result += weights[index]*getEi(i, (0.5*(x2-x1)*points[index] + (x2+x1)*0.5));
                }
            }
            result = result * 0.5 * (x2-x1);
        }

        return result;
    }

    //tutaj przymiarki do innego rozwiazania
    public double gaussianQuadrature3(int a, int i)
    {
        double[] points = { -0.774597, 0,  0.774597};
        double[] weights = { 0.555556,  0.888889, 0.555556};
        double result = 0;
        double x1 = getXi(i-1);
        double x2 = getXi(i+1);
        if (x1 < x2)
        {
            for (int index=0; index<3; index++)
            {
                result += weights[index]*a*getEiDerivative(i,  (((x2-x1)*0.5*points[index]) + 0.5*(x2+x1)));
            }
            result = result*(x2-x1)*0.5;
        }
        return result;
    }

    //liczenie calki dla funkcji B - metoda Gaussa-Legendre'a
    public double gaussianQuadrature(int i, int j, float begin, float end)
    {
        float[] points = {(float) -0.774597, 0, (float) 0.774597};
        float[] weights = {(float) 0.555556, (float) 0.888889, (float) 0.555556};
        double result = 0;

        if(begin<end && begin != Float.NEGATIVE_INFINITY)
        {
            for (int index = 0; index < 3; index++)
            {
                float x = points[index]*(begin - end)/2 + (begin+end)/2;
                result += weights[index]*getEiDerivative(i, x)*getEiDerivative(j, x);
            }
            result = result * 0.5 * (begin-end);
        }
        return result;
    }


    //obliczanie macierzy metoda eliminacji Gaussa
    public double[] gaussianElimination(double[][] matrixB, double[] matrixL)
    {
        double[][] matrix = new double[FEMSolver.n][FEMSolver.n+1];
        double[] result = new double[FEMSolver.n];
        double sum;
        double c;
        for(int i = 0; i<n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                matrix[i][j] = matrixB[i][j];
            }
            matrix[i][n] = matrixL[i];
        }
        for(int i =0; i<n-1; i++)
        {
            for(int j=i+1; j<n; j++)
            {
                c = matrix[j][i]/matrix[i][i];
                for (int k = 0; k < n+1; k++)
                {
                    matrix[j][k] -= c*matrix[i][k];
                }
            }
        }
        result[n-1] = matrix[n-1][n]/matrix[n-1][n-1];
        for (int i=n-2; i>=0; i--)
        {
            sum = 0;
            for(int j = i+1; j<n; j++)
            {
                sum += matrix[i][j]*result[j];
            }
            result[i] = (matrix[i][n]-sum)/matrix[i][i];
        }
        return result;
    }


    //rownanie
    public double countEquation(double[] result, float x)
    {
        double sum = 0;
        for(int i = 0; i<n; i++)
        {
            sum += result[i]*getEi(i, x);
        }
        return sum;
    }

    //rysowanie wykresu
    public void drawChart(double[] result)
    {
        float point = 0;
        while (point <= 3.0)
        {
            equation_chart.addData(point, countEquation(result, point));
            point += 0.1;
        }
    }
}
