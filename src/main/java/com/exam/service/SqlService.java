package com.exam.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SqlService {

    private final JdbcTemplate jdbcTemplate;

    public SqlService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public int executeUpdate(String sql) {
        return jdbcTemplate.update(sql);
    }
}
