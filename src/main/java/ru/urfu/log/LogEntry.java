package ru.urfu.log;

/**
 * Запись из лога
 */
public class LogEntry
{
    private final LogLevel logLevel;
    private final String strMessage;
    
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        this.strMessage = strMessage;
        this.logLevel = logLevel;
    }

    /**
     *
     * @return возвращает текст сообщения лога
     */
    public String getMessage()
    {
        return strMessage;
    }

    /**
     *
     * @return возвращает уровень сообщения
     */
    public LogLevel getLevel()
    {
        return logLevel;
    }
}

