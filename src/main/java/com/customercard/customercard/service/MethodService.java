package com.customercard.customercard.service;

import com.customercard.customercard.exception.ItemNotFoundException;
import com.customercard.customercard.model.Method;
import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.repository.MethodRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("methodService")
public class MethodService implements DictionaryService {

    private final MethodRepo repo;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodService.class);

    @Autowired
    public MethodService(MethodRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Method> getAll() {
        LOGGER.info("All methods fetched.");
        return repo.findAll();
    }

    @Override
    public Method getById(@NotNull String id) {
        LOGGER.info("Method fetched by id.");
        return repo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    public List<Method> getByName(@NotNull String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(method -> StringUtils.containsIgnoreCase(method.getName(), txt))
                .collect(Collectors.toList());
    }

    @Override
    public Dictionary create(Dictionary method) {

        return findFirstByName(method.getName())
                .orElseGet(() -> {
                    LOGGER.info("Method created.");
                    return repo.save((Method) method);
                });

    }

    @Override
    public Dictionary update(Dictionary method) {

        return findFirstByName(method.getName())
                .orElseGet(() -> {
                    LOGGER.info("Method updated.");
                    return repo.save((Method) method);
                });

    }

    @Override
    public boolean delete(String id) {
        LOGGER.info("Method deleted.");
        repo.deleteById(id);
        return true;
    }

    @Override
    public boolean partialUpdate(Dictionary method, Map<String, Object> updates) {
        LOGGER.info("Method particular updated.");
        if (updates.containsKey("name")) {
            method.setName((String) updates.get("name"));
        }
        update(method);
        return true;
    }

    @Override
    public Optional<Method> findFirstByName(String name) {
        return repo
                .findByName(name)
                .stream()
                .findFirst();
    }

}
