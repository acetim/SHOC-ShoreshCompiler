public class BindingPower {
    private Double left;
    private Double right;

    public BindingPower(Double left, Double right) {
        this.right = right;
        this.left = left;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public Double getRight() {
        return right;
    }

    public void setRight(Double right) {
        this.right = right;
    }
}
