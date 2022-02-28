package com.customercard.customercard.service;

import com.customercard.customercard.exception.ItemNotFoundException;
import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.repository.ColorRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("colorService")
public class ColorService implements DictionaryService {

    private final ColorRepo repo;
    private static final Logger LOGGER = LoggerFactory.getLogger(ColorService.class);

    @Autowired
    public ColorService(ColorRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Color> getAll() {
        LOGGER.info("All colors fetched.");
        return repo.findAll();
    }

    @Override
    public Color getById(@NotNull String id) {
        LOGGER.info("Color fetched by id.");
        return repo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    public List<Color> getByName(@NotNull String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(color -> StringUtils.containsIgnoreCase(color.getName(), txt))
                .collect(Collectors.toList());
    }

    @Override
    public Dictionary create(Dictionary color) {

        if (validateIfExists(color)) {
            return findFirstByName(color.getName());
        }

        LOGGER.info("Color created.");
        return repo.save((Color) color);

    }

    @Override
    public Dictionary update(Dictionary color) {

        if (validateIfExists(color)) {
            return findFirstByName(color.getName());
        }
        LOGGER.info("Color updated.");
        return repo.save((Color) color);
    }

    @Override
    public boolean delete(String id) {
        LOGGER.info("Color deleted.");
        repo.deleteById(id);
        return true;
    }

    @Override
    public boolean partialUpdate(Dictionary color, Map<String, Object> updates) {
        LOGGER.info("Color particular updated.");
        if (updates.containsKey("name")) {
            color.setName((String) updates.get("name"));
        }
        update(color);
        return true;
    }

    @Override
    public boolean validateIfExists(Dictionary color) {
        if (repo.findByName(color.getName()).size() > 0) {
            LOGGER.info("Color already exists.");
            return true;
        } else if (color.getName() == null || color.getName().equals("")) {
            LOGGER.info("Empty color passed.");
            return true;
        }
        return false;
    }

    @Override
    public Color findFirstByName(String name) {
        return repo
                .findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException(name));
    }

}
