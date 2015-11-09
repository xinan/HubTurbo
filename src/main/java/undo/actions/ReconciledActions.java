package undo.actions;

public class ReconciledActions {

    private final Action a;
    private final Action b;

    public ReconciledActions(Action a, Action b) {
        this.a = a;
        this.b = b;
    }

    public Action getA() {
        return a;
    }

    public Action getB() {
        return b;
    }

}
