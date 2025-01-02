package com.example.backend.mapper;

import org.springframework.stereotype.Component;

@Component
public interface AbstractDefault<T,K> {
    T mapToDto(K entity);
    K mapToEntity(T entity);
}