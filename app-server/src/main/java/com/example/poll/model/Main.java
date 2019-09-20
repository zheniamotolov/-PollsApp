package com.example.poll.model;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Q q = new Q();
        new Producer(q);
        new Consumer(q);
        Thread.sleep(100000);
        System.out.println("Press Control-C to stop.");
    }


}
