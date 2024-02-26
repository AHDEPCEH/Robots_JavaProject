package ru.urfu.log;

/**
 * Запись из лога
 */
public class LogEntry
{
    private LogLevel m_logLevel;
    private String m_strMessage;
    
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        m_strMessage = strMessage;
        m_logLevel = logLevel;
    }

    /**
     *
     * @return возвращает текст сообщения лога
     */
    public String getMessage()
    {
        return m_strMessage;
    }

    /**
     *
     * @return возвращает уровень сообщения
     */
    public LogLevel getLevel()
    {
        return m_logLevel;
    }
}

