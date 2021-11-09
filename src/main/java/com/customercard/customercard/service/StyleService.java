package com.customercard.customercard.service;

import com.customercard.customercard.model.Style;
import com.customercard.customercard.repository.StyleRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("styleService")
public class StyleService {

    private final StyleRepo repo;
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleService.class);

    @Autowired
    public StyleService(StyleRepo repo) {
        this.repo = repo;
    }

    public List<Style> getAllStyles() {
        LOGGER.info("All Styles fetched.");
        return repo.findAll();
    }

    public Style getStyleById(@Nullable String id) {
        LOGGER.info("Style fetched by id.");
        assert id != null;
        return repo.findById(id).orElse(new Style(""));
    }

    public List<Style> getStyleByName(@Nullable String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(style -> StringUtils.containsIgnoreCase(style.getName(), txt))
                .collect(Collectors.toList());
    }

    public List<Style> getStyles(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getStyleById(id));
        } else if (txt != null) {
            return getStyleByName(txt);
        } else {
            return getAllStyles();
        }
    }

    public Style createStyle(Style style) {

        if (validateIfExists(style)) {
            return findFirstByName(style.getName());
        }

        LOGGER.info("Style created.");
        return repo.save(style);
    }

    public Style updateStyle(Style style) {

        if (validateIfExists(style)) {
            return findFirstByName(style.getName());
        }

        LOGGER.info("Style updated.");
        return repo.save(style);
    }

    public boolean deleteStyle(String id) {
        LOGGER.info("Style deleted.");
        repo.deleteById(id);
        return true;
    }

    public boolean partialUpdate(Style style, Map<String, Object> updates) {
        LOGGER.info("Style particular updated.");
        if (updates.containsKey("name")) {
            style.setName((String) updates.get("name"));
        }
        updateStyle(style);
        return true;
    }

    public boolean validateIfExists(Style style) {
        if (repo.findByName(style.getName()).size() > 0) {
            LOGGER.info("Style already exists.");
            return true;
        }
        return false;
    }

    public Style findFirstByName(String name) {
        return repo
                .findByName(name)
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
