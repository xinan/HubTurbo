package util.events;

import backend.interfaces.IModel;
import backend.resource.MultiModel;
import backend.resource.TurboIssue;
import filter.expression.FilterExpression;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.Map;

public class ModelUpdatedEvent extends Event {
    public final IModel model;
    public final Map<FilterExpression, List<TurboIssue>> issuesToShow;

    public ModelUpdatedEvent(MultiModel models, Map<FilterExpression, List<TurboIssue>> issuesToShow) {
        this.model = models;
        this.issuesToShow = issuesToShow;
    }
}
