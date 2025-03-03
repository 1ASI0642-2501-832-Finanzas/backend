package me.ryzeon.finanzas.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.entity.Costs;
import me.ryzeon.finanzas.repository.CostsRepository;
import me.ryzeon.finanzas.service.CostsService;

@Service
@AllArgsConstructor
public class CostsServiceImpl implements CostsService {

    private final CostsRepository costsRepository;

    @Override
    public Costs saveCosts(Costs cost) {
        return costsRepository.save(cost);
    }

    @Override
    public List<Costs> saveAll(List<Costs> costs) {
        return costs.stream().map(this::saveCosts).toList();
    }

    @Override
    public void deleteCosts(Long id) {
        costsRepository.deleteById(id);
    }

    @Override
    public void deleteAllCosts(List<Long> ids) {
        ids.forEach(this::deleteCosts);
    }
}
