package com.example.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentStat<T,E> {
    private List<T> clazzList;
    private List<E> dataList;
}
