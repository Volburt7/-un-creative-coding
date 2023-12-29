package app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedArrayListSingleton {
    private static SharedArrayListSingleton instance;
    private final List<Long> PRIMES = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    private SharedArrayListSingleton() {
    }

    public static SharedArrayListSingleton getInstance() {
        if (instance == null) {
            synchronized (SharedArrayListSingleton.class) {
                if (instance == null) {
                    instance = new SharedArrayListSingleton();
                }
            }
        }
        return instance;
    }

    public void add(final Long value) {
        lock.lock();
        try {
            PRIMES.add(value);
        } finally {
            lock.unlock();
        }
    }

    public List<Long> getAll() {
        lock.lock();
        try {
            return new ArrayList<>(PRIMES);
        } finally {
            lock.unlock();
        }
    }
}
