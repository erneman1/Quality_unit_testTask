package com.qualityunit.test;

import com.qualityunit.test.executor.Executor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Executor executor = new Executor();
        executor.execute();
    }
}
