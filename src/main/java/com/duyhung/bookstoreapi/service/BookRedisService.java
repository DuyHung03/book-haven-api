package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.BookDto;
import com.duyhung.bookstoreapi.dto.BooksResponse;
import com.duyhung.bookstoreapi.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private String getSearchKey(String query, int pageNo, int pageSize) {
        return String.format("search_book:%s:%d:%d", query,pageNo,pageSize);
    }

    public void saveSearchBook(BooksResponse booksResponse, String query, int pageNo, int pageSize) throws JsonProcessingException {
        String key = getSearchKey(query, pageNo, pageSize);
        String data = objectMapper.writeValueAsString(booksResponse);
        redisTemplate.opsForValue().set(key, data);
    }

    public BooksResponse getSearchBook(String query, int pageNo, int pageSize) throws JsonProcessingException {
        String key = getSearchKey(query, pageNo, pageSize);
        String data = (String) redisTemplate.opsForValue().get(key);
        return data != null
                ? objectMapper.readValue(data, new TypeReference<BooksResponse>() {})
                : null;
    }

}
