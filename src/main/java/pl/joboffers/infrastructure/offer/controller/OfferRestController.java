package pl.joboffers.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class OfferRestController {

    private final OfferFacade offerFacade;
    @GetMapping("/offers")
    public ResponseEntity<JobOffersResponseDto> getOffers() {
        List<OfferResponseDto> allOffers = offerFacade.findAllOffers();
        JobOffersResponseDto jobOffers = new JobOffersResponseDto(allOffers);
        return ResponseEntity.ok(jobOffers);

    }

}
