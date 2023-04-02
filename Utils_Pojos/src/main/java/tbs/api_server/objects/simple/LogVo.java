package tbs.api_server.objects.simple;

import java.io.Serializable;
import java.math.BigDecimal;

public class LogVo implements Serializable {
    private final static long serialVersionUID = 1L;
    String function;
    double avg_cost;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public double getAvg_cost() {
        return avg_cost;
    }

    public void setAvg_cost(double avg_cost) {
        this.avg_cost = avg_cost;
    }
}
