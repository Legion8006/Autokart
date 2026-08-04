package com.autocart.businessservice.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.autocart.businessservice.entity.CarModel;
import com.autocart.businessservice.entity.CarVariant;
import com.autocart.businessservice.entity.FuelType;

@Repository
public interface CarVariantRepository extends JpaRepository<CarVariant, Long> {

    List<CarVariant> findByModel(CarModel model);

    Page<CarVariant> findAll(Pageable pageable);

    @Query(value = "SELECT DISTINCT v FROM CarVariant v " +
                   "JOIN FETCH v.model m JOIN FETCH m.brand b " +
                   "LEFT JOIN FETCH v.images " +
                   "WHERE (:brand IS NULL OR LOWER(b.name) = LOWER(:brand)) " +
                   "AND (:fuelType IS NULL OR v.fuelType = :fuelType) " +
                   "AND (:minPrice IS NULL OR v.basePrice >= :minPrice) " +
                   "AND (:maxPrice IS NULL OR v.basePrice <= :maxPrice)",
           countQuery = "SELECT COUNT(DISTINCT v) FROM CarVariant v " +
                        "JOIN v.model m JOIN m.brand b " +
                        "WHERE (:brand IS NULL OR LOWER(b.name) = LOWER(:brand)) " +
                        "AND (:fuelType IS NULL OR v.fuelType = :fuelType) " +
                        "AND (:minPrice IS NULL OR v.basePrice >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR v.basePrice <= :maxPrice)")
    Page<CarVariant> findAllFiltered(Pageable pageable,
                                     @Param("brand") String brand,
                                     @Param("fuelType") FuelType fuelType,
                                     @Param("minPrice") BigDecimal minPrice,
                                     @Param("maxPrice") BigDecimal maxPrice);

}