package com.museum.utils;

import javafx.collections.ObservableList;

@FunctionalInterface
public interface ListPopUpReturn<T> {
    void getReturn(ObservableList<T> input);
}
