package me.ryzeon.finanzas.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import me.ryzeon.finanzas.entity.Costs;


public interface CostsService {

    @Transactional
    Costs saveCosts(Costs cost);

    @Transactional
    List<Costs> saveAll(List<Costs> costs);


    @Transactional
    void deleteCosts(Long id);

    @Transactional
    void deleteAllCosts(List<Long> ids);
}