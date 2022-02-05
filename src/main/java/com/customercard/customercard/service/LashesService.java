package com.customercard.customercard.service;

import com.customercard.customercard.model.*;
import com.customercard.customercard.repository.LashesRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("lashesService")
public class LashesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LashesService.class);
    private final LashesRepo repo;
    private final MethodService methodService;
    private final StyleService styleService;
    private final ColorService colorService;

    @Autowired
    public LashesService(LashesRepo repo, MethodService methodService, StyleService styleService, ColorService colorService) {
        this.repo = repo;
        this.methodService = methodService;
        this.styleService = styleService;
        this.colorService = colorService;
    }

    public List<Lashes> getAll() {
        LOGGER.info("All Lashes fetched.");
        return repo.findAll();
    }

    public Lashes getById(@Nullable String id) {
        LOGGER.info("Lashes fetched by id.");
        assert id != null;
        return repo.findById(id).orElse(new Lashes());
    }

    public List<Lashes> getByComment(@Nullable String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(Lashes -> StringUtils.containsIgnoreCase(Lashes.getComment(), txt))
                .collect(Collectors.toList());

    }

    public List<Lashes> getAll(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getById(id));
        } else if (txt != null) {
            return getByComment(txt);
        } else {
            return getAll();
        }
    }

    public Lashes create(Lashes lashes) {
        LOGGER.info("Lashes created.");

        if (validateIfExists(lashes)) {
            return findFirstEqual(lashes);
        }

        setDates(lashes);
        setSubClasses(lashes);

        return repo.save(lashes);
    }


    //TODO test how to next date will act when i set it to null value
    private void setDates(Lashes lashes) {
        if (lashes.getDate() == null) {
            lashes.setDate(LocalDateTime.now());
        }
//        if (lashes.getNextDate() == null) {
//            lashes.setNextDate(LocalDateTime.now().plusWeeks(2));
//        }
    }

    public Lashes update(Lashes lashes) {

        if (validateIfExists(lashes)) {
            return findFirstEqual(lashes);
        }

        setSubClasses(lashes);
        LOGGER.info("Lashes updated.");
        return repo.save(lashes);
    }

    public boolean delete(String id) {
        LOGGER.info("Lashes deleted.");
        repo.deleteById(id);
        return true;
    }

    public boolean partialUpdate(Lashes lashes, Map<String, Object> updates) {
        LOGGER.info("Lashes particular updated.");
        if (updates.containsKey("style")) {
            lashes.setStyle((String) updates.get("style"));
        }
        if (updates.containsKey("method")) {
            lashes.setMethod((String) updates.get("method"));
        }
        if (updates.containsKey("color")) {
            lashes.setColor((String) updates.get("color"));
        }
        if (updates.containsKey("comment")) {
            lashes.setComment((String) updates.get("comment"));
        }
        if (updates.containsKey("date")) {
            lashes.setDate((LocalDateTime) updates.get("date"));
        }
        if (updates.containsKey("nextDate")) {
            lashes.setNextDate((LocalDateTime) updates.get("nextDate"));
        }
        update(lashes);
        return true;
    }

    private void setSubClasses(Lashes lashes) {
        getMethodClass(lashes);
        getStyleClass(lashes);
        getColorClass(lashes);
    }

    private void getColorClass(Lashes lashes) {
        if (!colorService.validateIfExists(new Color(lashes.getColor()))) {
            colorService.create(new Color(lashes.getColor()));
        }
    }

    private void getStyleClass(Lashes lashes) {
        if (!styleService.validateIfExists(new Style(lashes.getStyle()))) {
            styleService.create(new Style(lashes.getStyle()));
        }
    }

    private void getMethodClass(Lashes lashes) {
        if (!methodService.validateIfExists(new Method(lashes.getMethod()))) {
            methodService.create(new Method(lashes.getMethod()));
        }
    }

    public boolean validateIfExists(Lashes lashes) {

        if (findFirstEqual(lashes) != null) {
            LOGGER.info("Lashes already exists.");
            return true;
        }
        return false;
    }

    public Lashes findFirstEqual(Lashes lashes) {
        if (repo.findAll().size() > 0) {
            return repo.findAll().stream()
                    .filter(l -> l.equals(lashes))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public List<Lashes> getAll(@NotNull Customer customer, @Nullable String txt) {
        List<Lashes> lashesList = customer.getLashesList();

        if (txt != null) {
            return lashesList.stream()
                    .filter(lash -> StringUtils.containsIgnoreCase(lash.getStyle(), txt)
                            || StringUtils.containsIgnoreCase(lash.getMethod(), txt)
                            || StringUtils.containsIgnoreCase(lash.getColor(), txt)
                            || StringUtils.containsIgnoreCase(lash.getComment(), txt))
                    .collect(Collectors.toList());

        } else {
            return lashesList;
        }
    }

}
