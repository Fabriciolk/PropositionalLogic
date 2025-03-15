package propositionalLogic.proposition.notification;

import notification.GenericSubject;
import notification.NotificationData;
import notification.NotificationEvent;
import notification.Observer;
import propositionalLogic.proposition.Proposition;

public class PropertyNotification
{
    private final GenericSubject<NotificationEvent> genericSubject = new GenericSubject<>();
    private final Proposition propositionOwner;

    public PropertyNotification(Proposition owner)
    {
        propositionOwner = owner;
    }

    public void addObserver(NotificationEvent notificationEvent, Observer observer)
    {
        genericSubject.addObserver(notificationEvent, observer);
    }

    public void removeObserver(NotificationEvent notificationEvent, Observer observer)
    {
        genericSubject.removeObserver(notificationEvent, observer);
    }

    public void notify(NotificationEvent notificationEvent, NotificationData notificationData)
    {
        genericSubject.notifyEvent(notificationEvent, notificationData);
    }
}
