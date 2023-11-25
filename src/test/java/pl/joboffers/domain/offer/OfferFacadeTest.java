package pl.joboffers.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;
import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;

import java.util.List;
import java.util.Set;

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
        assertThat(false).isTrue();

    }

    @Test
    public void should_not_save_offer_to_database_which_exist() {
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
        List<OfferRequestDto> listOffers = List.of(
                new OfferRequestDto("company", "3000", "position", "url"),
                new OfferRequestDto("company", "3000", "position", "url2"),
                new OfferRequestDto("company", "3000", "position", "url3"),
                new OfferRequestDto("company", "3000", "position", "url4")
        );

        //when
        listOffers.forEach(offer -> offerFacade.saveOffer(offer));
        Set<OfferResponseDto> allOffers = offerFacade.findAllOffers();

        //then
        assertThat(allOffers.size()).isEqualTo(4);
        //how to better write it?
        assertThat(allOffers.contains(new OfferResponseDto("10", "company", "3000", "position", "url"))).isTrue();
        assertThat(allOffers.contains(new OfferResponseDto("12", "company", "3000", "position", "url2"))).isTrue();
        assertThat(allOffers.contains(new OfferResponseDto("15", "company", "3000", "position", "url3"))).isTrue();
        assertThat(allOffers.contains(new OfferResponseDto("24", "company", "3000", "position", "url4"))).isTrue();
    }

    @Test
    public void should_save_4_offers_when_there_are_no_offers_in_database() {
        assertThat(false).isTrue();
        //given
//        Offer offer = new Offer(1, "url");
//        Offer offer2 = new Offer(2, "url2");
//        Offer offer3 = new Offer(3, "url3");
//        Offer offer4 = new Offer(4, "url4");
//
//        //when
//        String result = offerFacade.saveOffer(offer);
//        String result2 = offerFacade.saveOffer(offer2);
//        String result3 = offerFacade.saveOffer(offer3);
//        String result4 = offerFacade.saveOffer(offer4);
//
//        //then
//        assertThat(result).isEqualTo("success");
//        assertThat(result2).isEqualTo("success");
//        assertThat(result3).isEqualTo("success");
//        assertThat(result4).isEqualTo("success");

    }

    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_urls() {
        assertThat(false).isTrue();
        //given
//        Offer offer = new Offer(1, "url");
//        Offer offer2 = new Offer(2, "url2");
//        Offer offer3 = new Offer(3, "url3");
//        Offer offer4 = new Offer(4, "url4");
//        String result = offerFacade.saveOffer(offer);
//        String result2 = offerFacade.saveOffer(offer2);
//        String result3 = offerFacade.saveOffer(offer3);
//        String result4 = offerFacade.saveOffer(offer4);
//        Offer offerToAdd1 = new Offer(1, "url");
//        Offer offerToAdd2 = new Offer(2, "url2");
//        Offer offerToAdd3 = new Offer(3, "url3");
//        Offer offerToAdd4 = new Offer(4, "url4");
//
//        //when
//        String result = offerFacade.saveOffer(offer);
//        String result2 = offerFacade.saveOffer(offer2);
//        String result3 = offerFacade.saveOffer(offer3);
//        String result4 = offerFacade.saveOffer(offer4);
//
//        //then
//        assertThat(result).isEqualTo("success");
//        assertThat(result2).isEqualTo("success");
//        assertThat(result3).isEqualTo("success");
//        assertThat(result4).isEqualTo("success");

    }

    @Test
    public void should_fetch_from_jobs_from_remote_and_save_all_offers_when_repository_is_empty() {
        assertThat(false).isTrue();
    }

}
