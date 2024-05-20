package ru.urfu.log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{

    private final CircularBuffer<LogEntry> messages;
    private final ArrayList<LogChangeListener> listeners;
    private volatile LogChangeListener[] activeLogListeners;
    
    public LogWindowSource(int queueLength) {
        messages = new CircularBuffer<>(queueLength);
        listeners = new ArrayList<>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(listeners)
        {
            listeners.add(listener);
            activeLogListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
            activeLogListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        messages.add(entry);
        LogChangeListener [] activeListeners = activeLogListeners;
        if (activeListeners == null)
        {
            synchronized (listeners)
            {
                if (activeLogListeners == null)
                {
                    activeListeners = listeners.toArray(new LogChangeListener [0]);
                    activeLogListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }
    
    public int size()
    {
        return messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return messages.iterator(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return messages.iterator(0, messages.size());
    }
}
