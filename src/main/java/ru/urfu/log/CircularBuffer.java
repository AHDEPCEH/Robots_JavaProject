package ru.urfu.log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Итерируемый лист по записям из сегмента
 * @param <T>
 */
class myList<T> implements Iterable<T> {
    private Segment<T> currentSegment;
    private final int lenSegment;
    private int current;
    private final int startIndex;
    private final int endIndex;
    private int count = 0;

    /**
     * Конструктор класса
     * @param segment - сегмент с которого начинается итерация
     * @param offset - сдвиг по количеству записей от начала
     * @param startIndex - начало записей
     * @param endIndex - конец записей
     * @param lenSegment - размер сегмента
     */
    public myList(Segment<T> segment, int offset, int startIndex, int endIndex, int lenSegment) {
        currentSegment = segment;
        current = startIndex + offset;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.lenSegment = lenSegment;
    }
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return count < endIndex - startIndex;
            }

            @Override
            public T next() {
                if (current >= lenSegment) {
                    for (int i = 0; i < current / lenSegment; i++) {
                        currentSegment = currentSegment.next;
                    }
                    current = current % lenSegment;
                }
                T element = currentSegment.entries.get(current);
                current++;
                count++;
                return element;
            }
        };
    }
}

/**
 * Сегмент содержащий несколько записей
 * @param <T> - тип хранимой записи
 */
class Segment<T>{
    public Segment<T> next;
    public List<T> entries = new LinkedList<>();

    public Segment(){}
}

/**
 * Кольцевой буфер ограниченного размера для хранения последних n добавленных объектов.
 * Состоит из нескольких сегментов, каждый из которых имеет ссылку на следующий сегмент.
 * @param <T> - Тип объекта
 */
public class CircularBuffer<T> {
    private Segment<T> tail;
    private Segment<T> head;
    private final int lenSegment;
    private final int maxLen;
    private int size;
    private final Lock lock = new ReentrantLock();

    /**
     * Конструктор класса
     * @param capacity - размер буфера
     */
    public CircularBuffer(int capacity) {
        head = new Segment<>();
        tail = head;
        maxLen = capacity;
        lenSegment = (int) Math.sqrt(capacity);
        this.size = 0;
    }

    /**
     * @return Возвращает количество элементов в буфере
     */
    public int size() {
        return Math.min(size, maxLen);
    }

    /**
     * Добавление элементов в буфер
     * @param element - элемент для добавления
     */
    public void add(T element) {
        lock.lock();
        try {
            if (size % lenSegment == 0 && size > 0) {
                head.next = new Segment<>();
                head = head.next;
                if (size > maxLen) {
                    tail = tail.next;
                    size -= lenSegment;
                }
            }
            size += 1;
            head.entries.add(element);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Итерация по буферу
     * @param startIndex - начало сегмента данных
     * @param endIndex - конец сегмента данных
     * @return Iterable объект
     */
    public Iterable<T> iterator(int startIndex, int endIndex) {
        lock.lock();
        try {
            return new myList<>(tail, Math.max(size - maxLen, 0), startIndex, endIndex, lenSegment);
        } finally {
            lock.unlock();
        }
    }
}

