package Parsing;

public class BindingPower {
    private final Double left;
    private final Double right;

    public BindingPower(Double left, Double right) {
        this.right = right;
        this.left = left;
    }

    public Double getLeft() {
        return left;
    }

    public Double getRight() {
        return right;
    }
}
