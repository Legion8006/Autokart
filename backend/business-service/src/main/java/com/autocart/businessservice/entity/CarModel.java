package com.autocart.businessservice.entity;

import java.time.LocalDateTime;

import com.autocart.businessservice.entity.BodyType;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(length = 36)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
	private List<CarVariant> variants;

	@Column(nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BodyType bodyType;

	private Integer launchYear;

	@Column(updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

}