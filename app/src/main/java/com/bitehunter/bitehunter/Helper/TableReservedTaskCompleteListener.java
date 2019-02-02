package com.bitehunter.bitehunter.Helper;

/**
 * Created by Jevin on 19-Oct-17.
 */

/**
 * This is selector_not_selected useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain selector_not_selected hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 */
public interface TableReservedTaskCompleteListener<T> {
    /**
     * Invoked when the AsyncTask has completed its execution.
     *
     * @param result The resulting object from the AsyncTask.
     */
    void tableReservedOnTaskComplete(T result);
}
