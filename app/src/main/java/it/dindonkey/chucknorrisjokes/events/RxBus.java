package it.dindonkey.chucknorrisjokes.events;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus
{
    private final Subject<Object, Object> mBusSubject = new SerializedSubject<>(PublishSubject.create());

    public <T> Subscription register(final Class<T> eventClass, Action1<T> onNext)
    {
        return mBusSubject
                .filter(new Func1<Object, Boolean>()
                {
                    @Override
                    public Boolean call(Object event)
                    {
                        return event.getClass().equals(eventClass);
                    }
                })
                .map(new Func1<Object, T>()
                {
                    @Override
                    public T call(Object o)
                    {
                        return (T) o;
                    }
                })
                .subscribe(onNext);
    }

    public void post(Object event)
    {
        mBusSubject.onNext(event);
    }
}
