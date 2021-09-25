package com.bgu.ecoway.adapters;

import java.util.List;

public interface BindableAdapter<T> {

    void setData(List<? extends T> newData);
}
