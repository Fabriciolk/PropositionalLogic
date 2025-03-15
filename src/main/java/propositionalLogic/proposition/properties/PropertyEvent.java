package propositionalLogic.proposition.properties;

import notification.NotificationEvent;

public class PropertyEvent implements NotificationEvent
{
    private final Property property;
    private final State state;

    public PropertyEvent(Property property, State state) {
        this.property = property;
        this.state = state;
    }

    @Override
    public String getEventID() {
        return property.name() + "/" + state.name();
    }
}
