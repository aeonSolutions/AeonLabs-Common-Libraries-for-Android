 package aeonlabs.common.libraries.activities;

public interface ActivityBaseObservable {
    void notifyObserversFragment(String TAG, String... args);
}