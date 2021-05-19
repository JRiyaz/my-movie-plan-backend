package com.MyMoviePlan.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "auditoriums")
public class AuditoriumEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @Column(length = 1000)
    private String image;

    @Column(length = 100)
    private String email;

    @Column(name = "customer_care_no", length = 50)
    private String customerCareNo;

    @Column(length = 200)
    private String address;

    @Column(name = "seat_capacity")
    private int seatCapacity;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "auditorium_facilities", joinColumns = @JoinColumn(name = "auditorium_id"))
    @Column(name = "facility")
    private List<String> facilities;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "auditorium_safeties", joinColumns = @JoinColumn(name = "auditorium_id"))
    @Column(name = "safety")
    private List<String> safeties;

    @ToString.Exclude
    @OneToMany(targetEntity = ShowEntity.class, cascade = CascadeType.ALL, mappedBy = "auditorium")
    private List<ShowEntity> shows;

    public AuditoriumEntity(String name, String image, String email, String customerCareNo, String address,
                            int seatCapacity, List<String> facilities, List<String> safeties, List<ShowEntity> shows) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.customerCareNo = customerCareNo;
        this.address = address;
        this.seatCapacity = seatCapacity;
        this.facilities = facilities;
        this.safeties = safeties;
        this.shows = shows;
    }

    public AuditoriumEntity setId(int id) {
        this.id = id;
        return this;
    }

    public AuditoriumEntity setName(String name) {
        this.name = name;
        return this;
    }

    public AuditoriumEntity setImage(String image) {
        this.image = image;
        return this;
    }

    public AuditoriumEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public AuditoriumEntity setCustomerCare(String customerCareNo) {
        this.customerCareNo = customerCareNo;
        return this;
    }

    public AuditoriumEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    public AuditoriumEntity setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
        return this;
    }

    public AuditoriumEntity setFacilities(List<String> facilities) {
        this.facilities = facilities;
        return this;
    }

    public AuditoriumEntity setSafeties(List<String> safeties) {
        this.safeties = safeties;
        return this;
    }

    public AuditoriumEntity setShows(List<ShowEntity> shows) {
        this.shows = shows;
        return this;
    }
}
