package pl.joboffers.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;
import pl.joboffers.domain.offer.exceptions.OfferUrlAlreadyExistException;

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
        OfferRequestDto offerToSave = new OfferRequestDto("1", "title", "company", "2000", "url");

        //when
        OfferResponseDto result = offerFacade.saveOffer(offerToSave);

        //then
        assertThat(result.offerUrl()).isEqualTo("url");

    }

    @Test
    public void should_not_save_offer_to_database_which_exist() {
        //given
        OfferRequestDto offerInDatabase = new OfferRequestDto("1", "title", "company", "2000", "url");
        OfferRequestDto offerToSave = new OfferRequestDto("1", "title", "company", "2000", "url");
        offerFacade.saveOffer(offerInDatabase);

        //when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(offerToSave));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferUrlAlreadyExistException.class)
                .hasMessage("Offer url already exist in database");
    }

    @Test
    public void should_find_offer_by_id() {
        //given
        OfferRequestDto offerInDatabase = new OfferRequestDto("1", "title", "company", "2000", "url");
        offerFacade.saveOffer(offerInDatabase);

        //when
        OfferResponseDto result = offerFacade.findOfferById(offerInDatabase.id());

        //then
        assertThat(result.id()).isEqualTo(offerInDatabase.id());
        assertThat(result.offerUrl()).isEqualTo(offerInDatabase.offerUrl());

    }

    @Test
    public void should_not_find_offer_by_id() {
        //given
        OfferRequestDto offerInDatabase = new OfferRequestDto("1", "title", "company", "2000", "url");

        //when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById(offerInDatabase.id()));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer not found");


    }

    @Test
    public void should_return_all_offers() {
        //given
//        List<Offer> listOffers = List.of(
//                new Offer(10, "urlName"),
//                new Offer(12, "urlName2"),
//                new Offer(15, "urlName3"),
//                new Offer(24, "urlName4")
//        );
//
//        //when
//        listOffers.forEach(offer -> offerFacade.saveOffer(offer));
//        Set<Offer> allOffers = offerFacade.findAllOffers();
//
//        //then
//        assertThat(allOffers.size()).isEqualTo(4);
//        assertThat(allOffers.contains(new Offer(10, "urlName"))).isTrue();
//        assertThat(allOffers.contains(new Offer(12, "urlName2"))).isTrue();
//        assertThat(allOffers.contains(new Offer(15, "urlName3"))).isTrue();
//        assertThat(allOffers.contains(new Offer(24, "urlName4"))).isTrue();
    }

    @Test
    public void should_not_return_all_offers() { // what we testing here??? -> test to change?
        //given
//        List<Offer> listOffers = List.of(
//                new Offer(10, "urlName"),
//                new Offer(12, "urlName2"),
//                new Offer(15, "urlName3"),
//                new Offer(24, "urlName4")
//        );
//
//        //when
//        listOffers.forEach(offer -> offerFacade.saveOffer(offer));
//        Set<Offer> allOffers = offerFacade.findAllOffers();
//
//        //then
//        assertThat(allOffers.size()).isEqualTo(4);
//        assertThat(allOffers.contains(new Offer(16, "urlName3"))).isFalse();
    }

    @Test
    public void should_save_4_offers_when_there_are_no_offers_in_database() {
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
    public void should_throw_duplicate_key_exception_when_with_offer_url_exists() {

    }

    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {

    }

    @Test
    public void should_fetch_from_jobs_from_remote_and_save_all_offers_when_repository_is_empty() {

    }

    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {

    }

}