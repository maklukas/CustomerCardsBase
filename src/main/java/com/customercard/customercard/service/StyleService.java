package com.customercard.customercard.service;

import com.customercard.customercard.exception.ItemNotFoundException;
import com.customercard.customercard.model.Style;
import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.repository.StyleRepo;
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

@Service("styleService")
public class StyleService implements DictionaryService {

    private final StyleRepo repo;
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleService.class);

    @Autowired
    public StyleService(StyleRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Style> getAll() {
        LOGGER.info("All styles fetched.");
        return repo.findAll();
    }

    @Override
    public Style getById(@NotNull String id) {
        LOGGER.info("Style fetched by id.");
        return repo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    public List<Style> getByName(@NotNull String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(style -> StringUtils.containsIgnoreCase(style.getName(), txt))
                .collect(Collectors.toList());
    }

    @Override
    public Dictionary create(Dictionary style) {

        return findFirstByName(style.getName())
                .orElseGet(() -> {
                    LOGGER.info("Style created.");
                    return repo.save((Style) style);
                });

    }

    @Override
    public Dictionary update(Dictionary style) {

        return findFirstByName(style.getName())
                .orElseGet(() -> {
                    LOGGER.info("Style updated.");
                    return repo.save((Style) style);
                });

    }

    @Override
    public boolean delete(String id) {
        LOGGER.info("Style deleted.");
        repo.deleteById(id);
        return true;
    }

    @Override
    public boolean partialUpdate(Dictionary style, Map<String, Object> updates) {
        LOGGER.info("Style particular updated.");
        if (updates.containsKey("name")) {
            style.setName((String) updates.get("name"));
        }
        update(style);
        return true;
    }

    @Override
    public Optional<Style> findFirstByName(String name) {
        return repo
                .findByName(name)
                .stream()
                .findFirst();
    }

}
