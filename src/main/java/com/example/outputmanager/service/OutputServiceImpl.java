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
    public List<Output> findByUserAndCategory(int userId, String categoryName) {
        return outputMapper.findByUserAndCategory(userId, categoryName);
    }

    @Override
    public List<Output> findByCategoryExcludingFavorite(int userId, int categoryId) {
        return outputMapper.findByUserAndCategoryExcludeFav(userId, categoryId);
    }

    @Override
    public List<Output> findRecentByUser(int userId, int limit) {
        return outputMapper.findRecentByUser(userId, limit);
    }

    @Override
    public Output findById(Long id) {
        return outputMapper.findById(id);
    }

    @Override
    public void save(Output output) {
        outputMapper.insert(output);
    }

    @Override
    public void update(Output output) {
        outputMapper.update(output);
    }

    @Override
    public void delete(Long id) {
        outputMapper.delete(id);
    }
}