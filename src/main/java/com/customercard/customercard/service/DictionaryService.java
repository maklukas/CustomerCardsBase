package com.customercard.customercard.service;

import com.customercard.customercard.model.Dictionary;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DictionaryService {

    List<? extends Dictionary> getAll();
    Dictionary getById(@NotNull String id);
    List<? extends Dictionary> getByName(@NotNull String txt);
    Dictionary create(Dictionary dictionary);
    Dictionary update(Dictionary dictionary);
    boolean delete(String id);
    boolean partialUpdate(Dictionary dictionary, Map<String, Object> updates);
    Optional<? extends Dictionary> findFirstByName(String name);

}
