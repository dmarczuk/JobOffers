package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
record Offer (
        Integer id,
        String title,
        String company,
        String salary,
        String offerUrl
) {}