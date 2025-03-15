package propositionalLogic.proposition.properties;

import notification.NotificationEvent;

public enum State implements NotificationEvent
{
    UPDATED(),
    REMOVED();

    @Override
    public String getEventID() {
        return this.name();
    }
}
