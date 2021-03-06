package com.codecool.navymanager.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "gun",
        uniqueConstraints = { @UniqueConstraint(
                name = "each_country_unique_gun_designation", columnNames = { "designation", "country_id" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Gun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "caliber_in_mms", nullable = false)
    private Integer caliberInMms;

    @Column(name = "projectile_weight_in_kgs", nullable = false)
    private Integer projectileWeightInKgs;

    @Column(name = "range_in_meters", nullable = false)
    private Integer rangeInMeters;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gun that = (Gun) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}