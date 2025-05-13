package com.backend.pnta.Services.Venues.Venue;

import com.backend.pnta.Models.Venues.Location.Location;
import com.backend.pnta.Models.Venues.Schedule.Schedule;
import com.backend.pnta.Models.Venues.Venue.PriceRating;
import com.backend.pnta.Models.Venues.Venue.Venue;
import com.backend.pnta.Models.Venues.Venue.VenueType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class VenueSpecifications {


    public static Specification<Venue> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Venue> subRoot = subquery.from(Venue.class);
            subquery.select(subRoot.get("venueId"))
                    .where(criteriaBuilder.like(criteriaBuilder.lower(subRoot.get("name")), "%" + name.toLowerCase() + "%"));
            return root.get("venueId").in(subquery);
        };
    }

    public static Specification<Venue> cityEquals(String city) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Location> subRoot = subquery.from(Location.class);
            subquery.select(subRoot.get("venue").get("venueId"))
                    .where(criteriaBuilder.like(criteriaBuilder.lower(subRoot.get("city")), "%" + city.toLowerCase() + "%"));
            return root.get("venueId").in(subquery);
        };
    }

    public static Specification<Venue> countryEquals(String country) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Location> subRoot = subquery.from(Location.class);
            subquery.select(subRoot.get("venue").get("venueId"))
                    .where(criteriaBuilder.equal(criteriaBuilder.lower(subRoot.get("country")), "%" + country.toLowerCase() + "%"));
            return root.get("venueId").in(subquery);
        };
    }


    public static Specification<Venue> scheduleWeekDayEquals(int weekDay) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Schedule> subRoot = subquery.from(Schedule.class);
            subquery.select(subRoot.get("venue").get("venueId"))
                    .where(
                            criteriaBuilder.equal(subRoot.get("venue").get("venueId"), root.get("venueId")),
                            criteriaBuilder.equal(subRoot.get("weekDay"), weekDay)
                    );
            return criteriaBuilder.exists(subquery);
        };
    }



    public static Specification<Venue> scheduleIsOpen(String time) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Schedule> subRoot = subquery.from(Schedule.class);
            subquery.select(subRoot.get("venue").get("venueId"))
                    .where(
                            criteriaBuilder.equal(subRoot.get("venue").get("venueId"), root.get("venueId")),
                            criteriaBuilder.lessThanOrEqualTo(subRoot.get("openingTime"), time),
                            criteriaBuilder.greaterThanOrEqualTo(subRoot.get("closingTime"), time)
                    );
            return criteriaBuilder.exists(subquery);
        };
    }

    /*
    //this id to filter the exact time
        public static Specification<Venue> scheduleClosingTimeEquals(String closingTime) {
            return (root, query, criteriaBuilder) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Schedule> subRoot = subquery.from(Schedule.class);
                subquery.select(subRoot.get("venue").get("venueId"))
                        .where(
                                criteriaBuilder.equal(subRoot.get("venue").get("venueId"), root.get("venueId")),
                                criteriaBuilder.equal(subRoot.get("closingTime"), closingTime)
                        );
                return criteriaBuilder.exists(subquery);
            };
        }
           public static Specification<Venue> scheduleHappyHourEquals(String happyHour) {
            return (root, query, criteriaBuilder) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Schedule> subRoot = subquery.from(Schedule.class);
                subquery.select(subRoot.get("venue").get("venueId"))
                        .where(
                                criteriaBuilder.equal(subRoot.get("venue").get("venueId"), root.get("venueId")),
                                criteriaBuilder.equal(subRoot.get("happyHour"), happyHour)
                        );
                return criteriaBuilder.exists(subquery);
            };
        }
    */
    public static Specification<Venue> priceRatingEquals(PriceRating priceRating) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priceRating"), priceRating);
    }
    public static Specification<Venue> ratingGreaterThanOrEqualTo(Double rating) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), rating);
    }

    public static Specification<Venue> ratingLessThanOrEqualTo(Double rating) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("rating"), rating);
    }

    public static Specification<Venue> typesIn(List<String> types) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<VenueType> subRoot = subquery.from(VenueType.class);
            subquery.select(subRoot.get("venue").get("venueId"))
                    .where(subRoot.get("type").in(types));
            return root.get("venueId").in(subquery);
        };
    }





}
