package com.autocart.businessservice.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variant_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private CarVariant variant;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType imageType;

}