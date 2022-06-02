package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.entity.GunAndQuantity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GunAndQuantityDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private ShipClassDto shipClass;
    private GunDto gun;
    private Integer quantity;

    public GunAndQuantityDto(GunAndQuantity gunAndQuantity) {
                id = gunAndQuantity.getId();
                gun = new GunDto(gunAndQuantity.getGun());
                quantity = gunAndQuantity.getGunQuantity();
    }

    public GunAndQuantity toEntity() {
        return new GunAndQuantity(id, gun.toEntity(), quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GunAndQuantityDto that = (GunAndQuantityDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "GunAndQuantityDto{" +
                "id=" + id +
                ", shipClass=" + shipClass +
                ", gun=" + gun +
                ", quantity=" + quantity +
                '}';
    }
}
