package pl.joboffers.domain.offer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OfferFacadeTest {

    OfferFacade offerFacade = new OfferFacade(
            new InMemoryOfferRepositoryTestImpl()
    );

    @Test
    public void should_save_offer_to_database() {
        //given
        Offer offer = new Offer(1, "url");

        //when
        String result = offerFacade.saveOffer(offer);

        //then
        assertThat(result).isEqualTo("success");

    }

    @Test
    public void should_not_save_offer_to_database_which_exist() {
        //given
        Offer firstOffer = new Offer(10, "urlName");
        Offer secondOffer = new Offer(10, "urlName2");

        //when
        offerFacade.saveOffer(firstOffer);
        String result = offerFacade.saveOffer(secondOffer);

        //then
        assertThat(result).isEqualTo("failed");

    }

    @Test
    public void should_find_offer_by_id() {
        //given
        Offer offer = new Offer(10, "urlName");

        //when
        offerFacade.saveOffer(offer);
        Offer resultOffer = offerFacade.findOfferById(offer.getId());

        //then
        assertThat(resultOffer).isNotNull();
        assertThat(resultOffer.getId()).isEqualTo(10);
        assertThat(resultOffer.getUrl()).isEqualTo("urlName");

    }

    @Test
    public void should_not_find_offer_by_id() {
        //given
        Offer offer = new Offer(10, "urlName");

        //when
        offerFacade.saveOffer(offer);
        Offer resultOffer = offerFacade.findOfferById(9);

        //then
        assertThat(resultOffer).isNull();

    }

    @Test
    public void should_return_all_offers() {
        //given
        List<Offer> listOffers = List.of(
                new Offer(10, "urlName"),
                new Offer(12, "urlName2"),
                new Offer(15, "urlName3"),
                new Offer(24, "urlName4")
        );

        //when
        List<Offer> allOffers = offerFacade.findAllOffers();

        //then
        assertThat(allOffers.size()).isEqualTo(4);
        assertThat(allOffers.get(0)).isEqualTo(new Offer(10, "urlName"));
        assertThat(allOffers.get(1)).isEqualTo(new Offer(12, "urlName2"));
        assertThat(allOffers.get(2)).isEqualTo(new Offer(15, "urlName3"));
        assertThat(allOffers.get(3)).isEqualTo(new Offer(24, "urlName4"));

    }

}