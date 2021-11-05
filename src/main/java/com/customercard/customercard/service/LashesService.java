package com.customercard.customercard.service;

import com.customercard.customercard.model.*;
import com.customercard.customercard.repository.LashesRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("lashesService")
public class LashesService {

    private static Logger LOGGER = LoggerFactory.getLogger(LashesService.class);
    private final LashesRepo repo;
    private final MethodService methodService;
    private final StyleService styleService;
    private final ColorService colorService;

    public LashesService(LashesRepo repo, MethodService methodService, StyleService styleService, ColorService colorService) {
        this.repo = repo;
        this.methodService = methodService;
        this.styleService = styleService;
        this.colorService = colorService;
    }

    @Autowired


    public List<Lashes> getAllLashes() {
        LOGGER.info("All Lashes fetched.");
        return repo.findAll();
    }

    public Lashes getLashesById(@Nullable String id) {
        LOGGER.info("Lashes fetched by id.");
        return repo.findById(id).orElse(new Lashes());
    }

    public List<Lashes> getLashesByComment(@Nullable String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(Lashes -> StringUtils.containsIgnoreCase(Lashes.getComment(), txt))
                .collect(Collectors.toList());

    }

    public List<Lashes> getLashes(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getLashesById(id));
        } else if (txt != null) {
            return getLashesByComment(txt);
        } else {
            return getAllLashes();
        }
    }

    public Lashes createLashes(Lashes lashes) {
        LOGGER.info("Lashes created.");

        if (validateIfExists(lashes)) {
            return findFirstEqual(lashes);
        }

        setDates(lashes);
        setSubClasses(lashes);

        return repo.save(lashes);
    }

    private void setDates(Lashes lashes) {
        if (lashes.getDate() == null) {
            lashes.setDate(LocalDate.now());
        }
        if (lashes.getNextDate() == null) {
            lashes.setDate(LocalDate.now().plusWeeks(2));
        }
    }

    public Lashes updateLashes(Lashes lashes) {

        if (validateIfExists(lashes)) {
            return findFirstEqual(lashes);
        }

        setSubClasses(lashes);
        LOGGER.info("Lashes updated.");
        return repo.save(lashes);
    }

    public boolean deleteLashes(String id) {
        LOGGER.info("Lashes deleted.");
        repo.deleteById(id);
        return true;
    }

    public boolean partialUpdate(Lashes lashes, Map<String, Object> updates) {
        LOGGER.info("Lashes particular updated.");
        if (updates.containsKey("style")) {
            lashes.setStyle((Style) updates.get("style"));
        }
        if (updates.containsKey("method")) {
            lashes.setMethod((Method) updates.get("method"));
        }
        if (updates.containsKey("color")) {
            lashes.setColor((Color) updates.get("color"));
        }
        if (updates.containsKey("comment")) {
            lashes.setComment((String) updates.get("comment"));
        }
        if (updates.containsKey("date")) {
            lashes.setDate((LocalDate) updates.get("date"));
        }
        if (updates.containsKey("nextDate")) {
            lashes.setNextDate((LocalDate) updates.get("nextDate"));
        }
        updateLashes(lashes);
        return true;
    }


    private void setSubClasses(Lashes lashes) {
        getMethodClass(lashes);
        getStyleClass(lashes);
        getColorClass(lashes);
    }

    private void getColorClass(Lashes lashes) {
        if (colorService.validateIfExists(lashes.getColor())) {
            lashes.setColor(colorService.findFirstByName(lashes.getColor().getName()));
        } else {
            lashes.setColor(colorService.createColor(lashes.getColor()));
        }
    }

    private void getStyleClass(Lashes lashes) {
        if (styleService.validateIfExists(lashes.getStyle())) {
            lashes.setStyle(styleService.findFirstByName(lashes.getStyle().getName()));
        } else {
            lashes.setStyle(styleService.createStyle(lashes.getStyle()));
        }
    }

    private void getMethodClass(Lashes lashes) {
        if (methodService.validateIfExists(lashes.getMethod())) {
            lashes.setMethod(methodService.findFirstByName(lashes.getMethod().getName()));
        } else {
            lashes.setMethod(methodService.createMethod(lashes.getMethod()));
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
                    .orElse(lashes);
        }
        return null;
    }

}
