package pl.joboffers.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;
import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class OfferFacadeTest {

    OfferFacade offerFacade = new OfferFacade(
            new InMemoryOfferRepositoryTestImpl(),
            new OfferService()
    );

    @Test
    public void should_save_offer_to_database() {
        //given
        OfferRequestDto offerToSave = new OfferRequestDto("company", "3000", "position", "url");

        //when
        OfferResponseDto result = offerFacade.saveOffer(offerToSave);

        //then
        assertThat(result.offerUrl()).isEqualTo("url");

    }

    @Test
    public void should_throw_duplicate_key_exception_when_with_offer_url_exists() {
        //given
        OfferRequestDto offerInDatabase = new OfferRequestDto("company", "3000", "position", "url");
        OfferRequestDto offerToSave = new OfferRequestDto("company", "3000", "position", "url");
        offerFacade.saveOffer(offerInDatabase);

        //when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(offerToSave));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferDuplicateException.class)
                .hasMessage("Offer url [" + offerToSave.offerUrl() + "] already exist in database");
    }

    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {
        //given
        OfferRequestDto offerInDatabase = new OfferRequestDto("company", "3000", "position", "url");
        OfferResponseDto savedOffer = offerFacade.saveOffer(offerInDatabase);

        //when
        OfferResponseDto result = offerFacade.findOfferById(savedOffer.id());

        //then
        assertThat(result).isEqualTo(savedOffer);

    }

    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {
        //given
        OfferRequestDto offerInDatabase = new OfferRequestDto("company", "3000", "position", "url");
        OfferResponseDto savedOffer = offerFacade.saveOffer(offerInDatabase);

        //when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById(savedOffer.id() + 1));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer not found");
    }

    @Test
    public void should_return_all_offers() {
        //given
        Set<OfferResponseDto> offersInDatabase = createDatabaseWith_4_Offers();

        //when
        Set<OfferResponseDto> allOffers = offerFacade.findAllOffers();

        //then
        assertThat(allOffers.size()).isEqualTo(4);
        assertThat(allOffers.containsAll(offersInDatabase)).isTrue();
    }

    @Test
    public void should_save_4_offers_when_there_are_no_offers_in_database() {
        //given
        List<OfferRequestDto> listOffers = List.of(
                new OfferRequestDto("company", "3000", "position", "url"),
                new OfferRequestDto("company", "3000", "position", "url2"),
                new OfferRequestDto("company", "3000", "position", "url3"),
                new OfferRequestDto("company", "3000", "position", "url4")
        );

        //when
        Set<OfferResponseDto> savedOffers = listOffers.stream()
                .map(offer -> offerFacade.saveOffer(offer))
                .collect(Collectors.toSet());

        //then
        assertThat(savedOffers.size()).isEqualTo(4);
    }

    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_urls() {
        //given
        Set<OfferResponseDto> offersInDatabase = createDatabaseWith_4_Offers();

        List<OfferRequestDto> listOffersToSave = List.of(
                new OfferRequestDto("company", "3000", "position", "url"),
                new OfferRequestDto("company", "3000", "position", "url2"),
                new OfferRequestDto("company", "3000", "position", "url3"),
                new OfferRequestDto("company", "3000", "position", "url4"),
                new OfferRequestDto("company", "3000", "position", "url5"),
                new OfferRequestDto("company", "3000", "position", "url6")
        );

        //when
        Set<OfferResponseDto> savedOffers = new HashSet<>();
        try {
            savedOffers = listOffersToSave.stream()
                    .map(offer -> offerFacade.saveOffer(offer))
                    .collect(Collectors.toSet());
        } catch (OfferDuplicateException e) {

        }

        //then
        assertThat(savedOffers.size()).isEqualTo(2);
        assertThat(savedOffers.stream()
                .filter(offer -> offer.offerUrl().equals("url5") || offer.offerUrl().equals("url6"))
                .count()).isEqualTo(2);

    }

    @Test
    public void should_fetch_from_jobs_from_remote_and_save_all_offers_when_repository_is_empty() {
        assertThat(false).isTrue();
    }

    private Set<OfferResponseDto> createDatabaseWith_4_Offers() {
        List<OfferRequestDto> listOffers = List.of(
                new OfferRequestDto("company", "3000", "position", "url"),
                new OfferRequestDto("company", "3000", "position", "url2"),
                new OfferRequestDto("company", "3000", "position", "url3"),
                new OfferRequestDto("company", "3000", "position", "url4")
        );
        return listOffers.stream()
                .map(offer -> offerFacade.saveOffer(offer))
                .collect(Collectors.toSet());
    }

}
