package com.customercard.customercard.service;

import com.customercard.customercard.model.Dictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface DictionaryService {

    List<? extends Dictionary> getAll();
    Dictionary getById(@NotNull String id);
    List<? extends Dictionary> getByName(@NotNull String txt);
    List<? extends Dictionary> getAll(@Nullable String id, @Nullable String txt);
    Dictionary create(Dictionary dictionary);
    Dictionary update(Dictionary dictionary);
    boolean delete(String id);
    boolean partialUpdate(Dictionary dictionary, Map<String, Object> updates);
    boolean validateIfExists(Dictionary dictionary);
    Dictionary findFirstByName(String name);

}
