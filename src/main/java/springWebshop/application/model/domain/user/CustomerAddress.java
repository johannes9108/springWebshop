package springWebshop.application.model.domain.user;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springWebshop.application.model.domain.order.Address;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CustomerAddress implements Address, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @JsonIgnore
    @ManyToOne
    private Customer customer;
    @NotBlank
    private String street;
    @NotNull
    private int zipCode;
    @NotBlank
    private String city;
    @NotBlank
    private String country;

    private AddressType addressType;

    private boolean isDefaultAddress;

    public CustomerAddress(String street, int zipCode, String city, String country) {
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }

    public void setAsDefault(){
        this.isDefaultAddress = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAddress that = (CustomerAddress) o;
        return zipCode == that.zipCode &&
                Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, zipCode, city, country);
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", addressType=" + addressType +
                ", defaultAddress=" + isDefaultAddress +
                '}';
    }
}
