package HTQ.BDS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "_property_listings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyListings extends BaseEntityAll{
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false,length = 100)
    private String city;

    @Column(length = 50)
    private String district;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type",nullable = false)
    private PropertyType propertyType;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "project_id", referencedColumnName = "id")
//    private Projects project;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_status",nullable = false)
    private ListingStatus listingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
