package de.ait.javalessons.threads;

/**
 * Класс DemoThreadRunnable реализует интерфейс Runnable
 * и демонстрирует работу многопоточного программирования в Java.
 */

public class DemoThreadRunnable implements Runnable {
    private Thread thread; // Переменная для хранения ссылки на поток
    private String threadName; // Имя потока

    //  Конструктор класса. Устанавливает имя потока и выводит сообщение о его создании.
    public DemoThreadRunnable(String threadName) {
        this.threadName = threadName;
        System.out.println("Creating" + threadName); // Вывод сообщения о создании потока
    }

    // Метод run() содержит код, который выполняется в отдельном потоке.
    @Override
    public void run() {
        System.out.println("Running " + threadName); // Вывод сообщения о запуске потока
        try {
            // Цикл с обратным отсчетом от 10 до 1
            for (int i = 10; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i); // Вывод текущего значения счетчика
                Thread.sleep(500); // Приостановка потока на 500 миллисекунд
            }
        } catch (InterruptedException exception) {
            // Обработка прерывания (interrupted) потока
            System.out.println("Thread " + threadName + " interrupted");
        }
        System.out.println("Thread " + threadName + " exiting"); // Завершение работы потока
    }

    //  Метод start() инициализирует и запускает поток, если он еще не был создан.
    public void start() {
        System.out.println("Starting " + threadName); // Вывод сообщения о запуске потока
        if (thread == null) { // Проверка, что поток еще не был создан
            thread = new Thread(this, threadName); // Создание нового потока
            thread.start(); // Запуск потока
        }
    }
}
