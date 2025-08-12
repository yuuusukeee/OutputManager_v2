package com.example.outputmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.mapper.OutputMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutputServiceImpl implements OutputService {

    private final OutputMapper outputMapper;

    @Override
    public List<Output> getOutputList(Integer userId) {
        return outputMapper.selectByUserId(userId);
    }

    @Override
    public List<Output> searchOutputs(String keyword, Integer categoryId, Integer userId) {
        return outputMapper.search(keyword, categoryId, userId);
    }

    @Override
    public Output getOutputById(Integer id) {
        return outputMapper.selectById(id);
    }

    @Override
    public int addOutput(Output out) {
        return outputMapper.insert(out); // 1想定
    }

    @Override
    public int updateOutput(Output out) {
        return outputMapper.update(out); // 0/1
    }

    @Override
    public int deleteOutput(Integer id) {
        return outputMapper.delete(id); // 0/1
    }
}