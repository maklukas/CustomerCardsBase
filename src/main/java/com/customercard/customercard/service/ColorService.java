package com.customercard.customercard.service;

import com.customercard.customercard.model.Color;
import com.customercard.customercard.repository.ColorRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("colorService")
public class ColorService {

    private final ColorRepo repo;
    private static Logger LOGGER = LoggerFactory.getLogger(ColorService.class);

    @Autowired
    public ColorService(ColorRepo repo) {
        this.repo = repo;
    }

    public List<Color> getAllColors() {
        LOGGER.info("All colors fetched.");
        return repo.findAll();
    }

    public Color getColorById(@Nullable String id) {
        LOGGER.info("Color fetched by id.");
        return repo.findById(id).orElse(new Color(""));
    }

    public List<Color> getColorByName(@Nullable String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(color -> StringUtils.containsIgnoreCase(color.getName(), txt))
                .collect(Collectors.toList());
    }

    public List<Color> getColors(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getColorById(id));
        } else if (txt != null) {
            return getColorByName(txt);
        } else {
            return getAllColors();
        }
    }

    public Color createColor(Color color) {

        if (validateIfExists(color)) {
            return findFirstByName(color.getName());
        }

        LOGGER.info("Color created.");
        return repo.save(color);

    }

    public Color updateColor(Color color) {

        if (validateIfExists(color)) {
            return findFirstByName(color.getName());
        }
        LOGGER.info("Color updated.");
        return repo.save(color);
    }

    public boolean deleteColor(String id) {
        LOGGER.info("Color deleted.");
        repo.deleteById(id);
        return true;
    }

    public boolean partialUpdate(Color color, Map<String, Object> updates) {
        LOGGER.info("Color particular updated.");
        if (updates.containsKey("name")) {
            color.setName((String) updates.get("name"));
        }
        updateColor(color);
        return true;
    }

    public boolean validateIfExists(Color color) {
        if (repo.findByName(color.getName()).size() > 0) {
            LOGGER.info("Color already exists.");
            return true;
        }
        return false;
    }

    public Color findFirstByName(String name) {
        return repo
                .findByName(name)
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
