package com.customercard.customercard.service;

import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.Method;
import com.customercard.customercard.model.Style;
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

@Service
public class LashesService {

    private final LashesRepo repo;
    private static Logger LOGGER = LoggerFactory.getLogger(LashesService.class);

    @Autowired
    public LashesService(LashesRepo repo) {
        this.repo = repo;
    }

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

    public boolean createLashes(Lashes lashes) {
        LOGGER.info("Lashes created.");

        if (lashes.getDate() == null) {
            lashes.setDate(LocalDate.now());
        }

        if (lashes.getNextDate() == null) {
            lashes.setDate(LocalDate.now().plusWeeks(2));
        }

        repo.save(lashes);
        return true;
    }

    public boolean updateLashes(Lashes lashes) {
        LOGGER.info("Lashes updated.");
        repo.save(lashes);
        return true;
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
}
