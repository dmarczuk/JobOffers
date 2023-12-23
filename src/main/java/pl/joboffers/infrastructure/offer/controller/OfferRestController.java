package pl.joboffers.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class OfferRestController {

    private final OfferFacade offerFacade;
    @GetMapping("/offers")
    public ResponseEntity<List<OfferResponseDto>> getOffers() {
        List<OfferResponseDto> allOffers = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffers);

    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponseDto> findOfferById(@PathVariable String id) {
        OfferResponseDto offerById = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offerById);
    }

    @PostMapping("/offers")
    public ResponseEntity<OfferResponseDto> addOffer(@RequestBody @Valid OfferRequestDto offerToSave) {
        OfferResponseDto savedOffer = offerFacade.saveOffer(offerToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
    }

}
