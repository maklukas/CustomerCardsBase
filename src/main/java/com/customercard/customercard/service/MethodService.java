package com.customercard.customercard.service;

import com.customercard.customercard.model.Method;
import com.customercard.customercard.repository.MethodRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MethodService {

    private final MethodRepo repo;
    private static Logger LOGGER = LoggerFactory.getLogger(MethodService.class);

    @Autowired
    public MethodService(MethodRepo repo) {
        this.repo = repo;
    }

    public List<Method> getAllMethods() {
        LOGGER.info("All Methods fetched.");
        return repo.findAll();
    }

    public Method getMethodById(@Nullable String id) {
        LOGGER.info("Method fetched by id.");
        return repo.findById(id).orElse(new Method(""));
    }

    public List<Method> getMethodByName(@Nullable String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(method -> StringUtils.containsIgnoreCase(method.getName(), txt))
                .collect(Collectors.toList());
    }

    public List<Method> getMethods(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getMethodById(id));
        } else if (txt != null) {
            return getMethodByName(txt);
        } else {
            return getAllMethods();
        }
    }

    public boolean createMethod(Method method) {
        LOGGER.info("Method created.");
        repo.save(method);
        return true;
    }

    public boolean updateMethod(Method method) {
        LOGGER.info("Method updated.");
        repo.save(method);
        return true;
    }

    public boolean deleteMethod(String id) {
        LOGGER.info("Method deleted.");
        repo.deleteById(id);
        return true;
    }

    public boolean partialUpdate(Method method, Map<String, Object> updates) {
        LOGGER.info("Method particular updated.");
        if (updates.containsKey("name")) {
            method.setName((String) updates.get("name"));
        }
        updateMethod(method);
        return true;
    }
}