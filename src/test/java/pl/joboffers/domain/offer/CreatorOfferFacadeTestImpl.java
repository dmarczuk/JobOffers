package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class CreatorOfferFacadeTestImpl {



    public static OfferFacade createOfferFacadeForTest() {
        InMemoryOfferRepositoryTestImpl database = new InMemoryOfferRepositoryTestImpl();
        InMemoryFetcherTestImpl fetcher = new InMemoryFetcherTestImpl(
                Set.of(
                        new JobOfferResponse("companyName1", "position1", "2000", "url1"),
                        new JobOfferResponse("companyName2", "position2", "2000", "url2"),
                        new JobOfferResponse("companyName3", "position3", "2000", "url3"),
                        new JobOfferResponse("companyName4", "position4", "2000", "url4"),
                        new JobOfferResponse("Comarch", "junior", "4000", "http://comarchOffer.pl"),
                        new JobOfferResponse("Motorola", "mid", "7000", "http://motorolaOffer.pl")
                )
        );
        fetcher.fetchOffers();
        OfferService offerService = new OfferService(fetcher, database);
        return new OfferFacade(database, offerService);
    }

    public static Set<OfferResponseDto> createDatabaseWith_4_Offers(OfferFacade offerFacade) {
        List<OfferRequestDto> listOffers = List.of(
                new OfferRequestDto("company1", "2000", "position1", "url1"),
                new OfferRequestDto("company2", "2000", "position2", "url2"),
                new OfferRequestDto("company3", "2000", "position3", "url3"),
                new OfferRequestDto("company4", "2000", "position4", "url4")
        );
        return listOffers.stream()
                .map(offerFacade::saveOffer)
                .collect(Collectors.toSet());
    }

}
