package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;

//@AllArgsConstructor
@Builder
record Offer(String id,
              String companyName,
              String position,
              String salary,
              String offerUrl) { }