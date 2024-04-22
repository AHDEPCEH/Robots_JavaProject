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
     * Возвращает кусок буфера в заданном диапазоне
     * @param startIndex - начало диапазона
     * @param endIndex - конец диапазона
     * @return Лист с элементами буфера
     */
    public Iterable<T> getSegment(int startIndex, int endIndex) {
        lock.lock();
        try {
            List<T> segment = new ArrayList<>();
            int current = (head + startIndex) % buffer.length;
            for (int i = 0; i <= endIndex - startIndex; i++) {
                segment.add(buffer[current]);
                current = (current + 1) % buffer.length;
            }
            return segment;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает сам буфер с записями в правильном порядке
     * @return Лист с записями буфера
     */
    public Iterable<T> getAll() {
        lock.lock();
        try {
            List<T> allElements = new ArrayList<>();
            int current = head;
            for (int i = 0; i < size; i++) {
                allElements.add(buffer[current]);
                current = (current + 1) % buffer.length;
            }
            return allElements;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает итератор для буфера
     * @return Итератор
     */
    public Iterable<T> iterator() {
        lock.lock();
        try {
            return new Iterable<T>() {
                private int current = head;
                private int count = 0;

                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        @Override
                        public boolean hasNext() {
                            return count < size;
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

