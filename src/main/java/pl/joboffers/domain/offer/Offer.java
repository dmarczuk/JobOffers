package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

//@AllArgsConstructor
@Builder
@Document("offers")
record Offer(
        @Id String id,
        @Field("company") String companyName,
        @Field("position") String position,
        @Field("salary") String salary,
        @Field("url") @Indexed(unique = true) String offerUrl) {

}