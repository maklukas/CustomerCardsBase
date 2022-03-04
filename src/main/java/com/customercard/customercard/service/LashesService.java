package com.customercard.customercard.service;

import com.customercard.customercard.model.*;
import com.customercard.customercard.repository.LashesRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public Lashes create(Lashes lashes) {
        LOGGER.info("Lashes created.");

        return findFirstEqual(lashes)
                .orElseGet(() -> {
                            setDates(lashes);
                            setSubClasses(lashes);
                            return repo.save(lashes);
                        }
                );
    }

    private void setDates(Lashes lashes) {
        if (lashes.getDate() == null) {
            lashes.setDate(LocalDateTime.now());
        }
    }

    public Lashes update(Lashes lashes) {
        LOGGER.info("Lashes updated.");
        return findFirstEqual(lashes)
                .orElseGet(() -> {
                            setSubClasses(lashes);
                            return repo.save(lashes);
                        }
                );
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
        colorService.findFirstByName(lashes.getColor())
                .orElseGet(() -> (Color) colorService.create(new Color(lashes.getColor())));
    }

    private void getStyleClass(Lashes lashes) {
        styleService.findFirstByName(lashes.getColor())
                .orElseGet(() -> (Style) styleService.create(new Style(lashes.getStyle())));
    }

    private void getMethodClass(Lashes lashes) {
        methodService.findFirstByName(lashes.getColor())
                .orElseGet(() -> (Method) methodService.create(new Method(lashes.getMethod())));
    }

    private Optional<Lashes> findFirstEqual(Lashes lashes) {
        return repo.findOne(Example.of(lashes));
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
