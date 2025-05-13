package com.backend.pnta.Models.Venues.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Double latitude;
    private Double longitude;
    private String city;
    private String postalCode;
    private String country;
    private String address;
}
