package com.example.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOption<T,E> {
    private List<T> jobList;
    private List<E> dataList;
}
