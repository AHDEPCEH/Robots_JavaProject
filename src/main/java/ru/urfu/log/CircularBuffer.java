package ru.urfu.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Кольцевой буфер ограниченного размера для хранения последних n добавленных объектов
 * @param <T> - Тип объекта
 */
public class CircularBuffer<T> {
    private final T[] buffer;
    private int size;
    private int head;
    private int tail;
    private final Lock lock = new ReentrantLock();

    /**
     * Конструктор класса
     * @param capacity - размер буфера
     */
    public CircularBuffer(int capacity) {
        this.buffer = (T[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    /**
     * @return Возвращает количество элементов в буфере
     */
    public int size() {
        return size;
    }

    /**
     * Добавление элементов в буфер
     * @param element
     */
    public void add(T element) {
        lock.lock();
        try {
            if (size == buffer.length) {
                head = (head + 1) % buffer.length; // удаление старого элемента
            } else {
                size++;
            }
            buffer[tail] = element;
            tail = (tail + 1) % buffer.length;
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
            return new Iterable<>() {
                private int current = startIndex;
                private int count = 0;

                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        @Override
                        public boolean hasNext() {
                            return count < endIndex - startIndex;
                        }

                        @Override
                        public T next() {
                            T element = buffer[current];
                            current = (current + 1) % buffer.length;
                            count++;
                            return element;
                        }
                    };
                }
            };
        } finally {
            lock.unlock();
        }
    }
}

