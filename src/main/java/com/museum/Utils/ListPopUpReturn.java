package com.museum.Utils;

import javafx.collections.ObservableList;

@FunctionalInterface
public interface ListPopUpReturn<T> {
    void getReturn(ObservableList<T> input);
}
