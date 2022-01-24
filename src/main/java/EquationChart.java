import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class EquationChart {

    NumberAxis x_axis = new NumberAxis();
    NumberAxis y_axis = new NumberAxis();
    LineChart<Number, Number> equation_chart = new LineChart<>(x_axis, y_axis);
    XYChart.Series<Number, Number> chart_series = new XYChart.Series<>();
    public EquationChart(String x_axis_name, String y_axis_name)
    {
        x_axis.setLabel(x_axis_name);
        y_axis.setLabel(y_axis_name);
        equation_chart.getData().add(chart_series);
        equation_chart.setLegendVisible(false);
    }

    public void addData(Number x, Number y)
    {
        chart_series.getData().add(new XYChart.Data<>(x, y));
    }

    public LineChart<Number, Number> getChart()
    {
        return equation_chart;
    }
}
