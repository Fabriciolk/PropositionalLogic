package notification;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenericSubject<T extends NotificationEvent> implements Subject<T>
{
    private final Map<String, List<Observer>> observers = new HashMap<>();

    @Override
    public void addObserver(T eventType, Observer observer)
    {
        if (observer == null) return;
        String eventTypeKey = eventType.getEventID();

        if (!observers.containsKey(eventTypeKey))
        {
            observers.put(eventTypeKey, new LinkedList<>(List.of(observer)));
        }
        else
        {
            observers.get(eventTypeKey).add(observer);
        }
    }

    @Override
    public void removeObserver(T eventType, Observer observer)
    {
        if (observers.containsKey(eventType.getEventID())) observers.get(eventType.getEventID()).remove(observer);
    }

    public void notifyEvent(T eventType, NotificationData data)
    {
        if (!observers.isEmpty())
        {
            for (Observer observer : observers.get(eventType.getEventID())) observer.receiveNotification(data);
        }
    }
}
