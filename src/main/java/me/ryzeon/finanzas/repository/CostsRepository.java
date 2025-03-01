package me.ryzeon.finanzas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.ryzeon.finanzas.entity.Costs;

@Repository
public interface CostsRepository extends JpaRepository<Costs, Long> {

}
