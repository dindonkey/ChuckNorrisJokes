package it.dindonkey.chucknorrisjokes.data;

import rx.Scheduler;

public class SchedulerManager
{
    private final Scheduler computation;
    private final Scheduler mainThread;

    public SchedulerManager(Scheduler computation, Scheduler mainThread)
    {
        this.computation = computation;
        this.mainThread = mainThread;
    }

    public Scheduler computation()
    {
        return computation;
    }

    public Scheduler mainThread()
    {
        return mainThread;
    }
}
