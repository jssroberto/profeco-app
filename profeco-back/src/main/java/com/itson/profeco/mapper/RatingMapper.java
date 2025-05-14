package com.itson.profeco.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import com.itson.profeco.api.dto.request.RatingRequest;
import com.itson.profeco.api.dto.response.RatingResponse;
import com.itson.profeco.model.Rating;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapper {
    @Mappings({@Mapping(source = "customer.id", target = "customerId"),
            @Mapping(source = "customer.name", target = "customerName")})
    RatingResponse toRatingResponse(Rating rating);

    List<RatingResponse> toRatingResponseList(List<Rating> ratings);

    @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "date", ignore = true),
            @Mapping(target = "store", ignore = true),
            @Mapping(target = "customer", ignore = true)})
    Rating toRatingEntity(RatingRequest ratingRequest);



}
