package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

//@AllArgsConstructor
@Builder
@Document
record Offer(String id,
              String companyName,
              String position,
              String salary,
              String offerUrl) { }