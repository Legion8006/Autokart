package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dealers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dealer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    @Column(
        name = "showroom_name",
        nullable = false,
        length = 150
    )
    private String showroomName;

    @Column(
        name = "gst_number",
        nullable = false,
        unique = true,
        length = 15
    )
    private String gstNumber;

    @Column(
    	    name = "license_number",
    	    nullable = false,
    	    unique = true,
    	    length = 11
    	)
    	private String licenseNumber;
}