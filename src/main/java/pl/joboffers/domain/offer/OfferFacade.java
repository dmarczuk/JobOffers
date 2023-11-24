package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferNotFound;
import pl.joboffers.domain.offer.exceptions.OfferUrlAlreadyExistException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferService offerService;

    public Set<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(offer -> new OfferResponseDto(offer.id(), true, offer.offerUrl()))
                .collect(Collectors.toSet());
    }

    public OfferResponseDto findOfferById(Integer id) {
        return offerRepository.findById(id)
                .map(offer -> new OfferResponseDto(offer.id(), true, offer.offerUrl()))// add other parameters
                .orElseThrow(() -> new OfferNotFound("Offer not found"));
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerRequestDto) {
        //final Offer offer = Offer.builder().build;
        Offer offerSaved = offerRepository.save(new Offer(1, "", "", "", ""));
        return new OfferResponseDto(offerSaved.id(), true, offerSaved.offerUrl());
//        if (findOfferById(offerSaved.id()) == null) {
//            offerRepository.save(offerSaved);
//            return "success";
//        } else {
//            return "offer exist in database";
//        }
    }

    public Set<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {

        return new HashSet<>();
    }
}

//    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
//        final User user = User.builder()
//                .username(registerUserDto.username())
//                .password(registerUserDto.password())
//                .email(registerUserDto.email())
//                .build();
//        User savedUser = userRepository.save(user);
//        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());
//    }