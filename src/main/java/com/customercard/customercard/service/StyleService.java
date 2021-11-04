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

@Service
public class StyleService {

    private final StyleRepo repo;
    private static Logger LOGGER = LoggerFactory.getLogger(StyleService.class);

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

    public boolean createStyle(Style style) {
        LOGGER.info("Style created.");
        repo.save(style);
        return true;
    }

    public boolean updateStyle(Style style) {
        LOGGER.info("Style updated.");
        repo.save(style);
        return true;
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
}
