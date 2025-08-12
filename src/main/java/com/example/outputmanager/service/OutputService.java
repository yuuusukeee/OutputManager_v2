package com.example.outputmanager.service;

import java.util.List;

import com.example.outputmanager.domain.Output;

public interface OutputService {
    List<Output> getOutputList(Integer userId);
    List<Output> searchOutputs(String keyword, Integer categoryId, Integer userId);
    Output getOutputById(Integer id);

    int addOutput(Output out);      // INSERT件数（=1想定）
    int updateOutput(Output out);   // UPDATE件数（0/1）
    int deleteOutput(Integer id);   // DELETE件数
}