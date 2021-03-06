package de.rwth.idsg.brsm.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A BikeStation.
 */
@Entity
@Table(name = "T_BIKE_STATION")
public class BikeStation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_zip")
    private String addressZip;

    @Column(name = "opening_hours")
    private String openingHours;


    @Column(name = "location_latitude")
    private BigDecimal locationLatitude;

    @Column(name = "location_longitude")
    private BigDecimal locationLongitude;

    @ManyToOne
    @JoinColumn(name = "user_login")
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "bikeStation", orphanRemoval = true)
    @JsonManagedReference
    private Set<Bike> bikes;

//    @PreRemove
//    public void onDelete() {
//        for (Bike bike : getBikes()) {
//            bike.setBikeStation(null);
//        }
//    }

    @Formula("(SELECT count(*) FROM t_bike AS b WHERE b.bike_station_id = id AND b.is_rented = 'false')")
    private int numberOfAvailableBikes;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    protected BikeStation() {}
//
//    public BikeStation(String name, String addressStreet, String addressCity, String addressZip, String openingHours, int numberPorts) {
//        this.name = name;
//        this.addressCity = addressCity;
//        this.addressStreet = addressStreet;
//        this.addressZip = addressZip;
//        this.openingHours = openingHours;
//        this.numberPorts = numberPorts;
//    }

    public boolean addBike(Bike bike) {
        bike.setBikeStation(this);
        return bikes.add(bike);
    }

    public boolean removeBike(Bike bike) {
        return bikes.remove(bike);
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public BigDecimal getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(BigDecimal locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public BigDecimal getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(BigDecimal locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Set<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(Set<Bike> bikes) {
        this.bikes = bikes;
    }

    public int getNumberOfBikes() {
        return bikes.size();
    }

    public int getNumberOfAvailableBikes() { return numberOfAvailableBikes; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BikeStation bikestation = (BikeStation) o;

        if (id != bikestation.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "BikeStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressZip='" + addressZip + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", longitude='" + locationLongitude + '\'' +
                ", latitude='" + locationLatitude +
                '}';
    }
}
