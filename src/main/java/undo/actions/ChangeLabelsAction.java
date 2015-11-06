package undo.actions;

import backend.resource.TurboIssue;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ChangeLabelsAction implements Action<TurboIssue> {

    public static final String DESCRIPTION = "change label(s)";

    private TurboIssue originalIssue;
    private Set<String> addedLabels;
    private Set<String> removedLabels;

    public ChangeLabelsAction(TurboIssue issue, List<String> addedLabels, List<String> removedLabels) {
        originalIssue = new TurboIssue(issue);
        this.addedLabels = new HashSet<>(addedLabels);
        this.removedLabels = new HashSet<>(removedLabels);
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public TurboIssue act(TurboIssue issue) {
        Set<String> newLabels = new TreeSet<>(issue.getLabels());
        newLabels.addAll(addedLabels);
        newLabels.removeAll(removedLabels);
        issue.setLabels(newLabels.stream().collect(Collectors.toList()));
        return issue;
    }

    @Override
    public TurboIssue undo(TurboIssue issue) {
        Set<String> newLabels = new TreeSet<>(issue.getLabels());
        newLabels.addAll(removedLabels);
        newLabels.removeAll(addedLabels);
        issue.setLabels(newLabels.stream().collect(Collectors.toList()));
        return issue;
    }

    @Override
    public Pair<Action, Action> reconcile(Action a, Action b) {
        if (a.getClass().equals(this.getClass()) && b.getClass().equals(this.getClass())) {
            ChangeLabelsAction actionA = (ChangeLabelsAction) a;
            ChangeLabelsAction actionB = (ChangeLabelsAction) b;
            if (actionA.getOriginalIssue().getRepoId().equals(actionB.getOriginalIssue().getRepoId()) &&
                    actionA.getOriginalIssue().getId() == actionB.getOriginalIssue().getId()) {
                ChangeLabelsAction newActionA = new ChangeLabelsAction(actionA.getOriginalIssue(),
                        actionA.getAddedLabels().stream()
                                .filter(addedLabel -> !actionB.getRemovedLabels().contains(addedLabel))
                                .collect(Collectors.toList()),
                        actionA.getRemovedLabels().stream()
                                .filter(removedLabel -> !actionB.getAddedLabels().contains(removedLabel))
                                .collect(Collectors.toList()));
                return new Pair<>(newActionA,
                        new ChangeLabelsAction(newActionA.act(actionA.getOriginalIssue()),
                                actionB.getAddedLabels().stream()
                                        .filter(addedLabel -> !actionA.getRemovedLabels().contains(addedLabel))
                                        .collect(Collectors.toList()),
                                actionB.getRemovedLabels().stream()
                                        .filter(removedLabel -> !actionA.getAddedLabels().contains(removedLabel))
                                        .collect(Collectors.toList())));
            }
        }
        return new Pair<>(a, b);
    }

    @Override
    public boolean isNoOp() {
        return addedLabels.isEmpty() && removedLabels.isEmpty();
    }

    public Set<String> getAddedLabels() {
        return addedLabels;
    }

    public Set<String> getRemovedLabels() {
        return removedLabels;
    }

    public TurboIssue getOriginalIssue() {
        return originalIssue;
    }

    public static ChangeLabelsAction createChangeLabelsAction(TurboIssue issue, List<String> newLabels) {
        return new ChangeLabelsAction(issue,
                newLabels.stream()
                        .filter(newLabel -> !issue.getLabels().contains(newLabel))
                        .collect(Collectors.toList()),
                issue.getLabels().stream()
                        .filter(originalLabel -> !newLabels.contains(originalLabel))
                        .collect(Collectors.toList()));
    }

}
