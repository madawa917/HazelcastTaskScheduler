package com.madawa.scheduler.task;

import java.io.Serializable;

public class MorningTask implements Runnable, Serializable {

    private static final long serialVersionUID = -3727837260502261351L;

    @Override
    public void run() {
        // Your logics..
        System.out.println("Running MorningTask...");
    }
}
