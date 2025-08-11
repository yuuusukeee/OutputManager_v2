package com.example.outputmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.mapper.OutputMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class OutputServiceImpl implements OutputService {

    private final OutputMapper outputMapper;

    @Override
    public List<Output> getOutputList(Integer userId) {
        List<Output> list = outputMapper.search(null, null, userId);
        System.out.println("[DEBUG] mapper outputs size=" + (list == null ? "null" : list.size())
                           + " userId=" + userId);
        return list;
    }

    @Override
    public Output getOutputById(Integer id) {
        return outputMapper.selectById(id);
    }

    @Override
    public void addOutput(Output output) {
        outputMapper.insert(output);
    }

    @Override
    public void updateOutput(Output output) {
        outputMapper.update(output);
    }

    @Override
    public void deleteOutput(Integer id) {
        outputMapper.delete(id);
    }

    @Override
    public List<Output> searchOutputs(String keyword, Integer categoryId, Integer userId) {
        return outputMapper.search(keyword, categoryId, userId);
    }
}