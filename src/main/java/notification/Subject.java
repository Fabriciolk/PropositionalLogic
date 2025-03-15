package notification;

public interface Subject<T extends NotificationEvent>
{
    void addObserver(T eventType, Observer observer);

    void removeObserver(T eventType, Observer observer);
}
