package undo.actions;

public interface Action<T> {

    String getDescription();

    T act(T t);

    T undo(T t);

    static ReconciledActions reconcile(Action a, Action b) {
        return new ReconciledActions(a, b);
    }

    boolean isNoOp();

    @SuppressWarnings("unchecked")
    default Action invert() {
        Action that = this;
        return new Action<T>() {
            @Override
            public String getDescription() {
                return that.getDescription();
            }

            @Override
            public T act(T t) {
                return (T) that.undo(t);
            }

            @Override
            public T undo(T t) {
                return (T) that.act(t);
            }

            @Override
            public boolean isNoOp() {
                return that.isNoOp();
            }
        };
    }

}
