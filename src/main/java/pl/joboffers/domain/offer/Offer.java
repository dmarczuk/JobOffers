package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;

//@AllArgsConstructor
@Builder
record Offer(String id,
              String title,
              String company,
              String salary,
              String offerUrl) { }