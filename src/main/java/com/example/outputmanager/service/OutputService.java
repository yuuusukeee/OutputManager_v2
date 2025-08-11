package com.example.outputmanager.service;

import com.example.outputmanager.domain.Output;
import java.util.List;

public interface OutputService {
    List<Output> getOutputList(Integer userId);
    Output getOutputById(Integer id);
    void addOutput(Output output);
    void updateOutput(Output output);
    void deleteOutput(Integer id);
    List<Output> searchOutputs(String keyword, Integer categoryId, Integer userId);
}
