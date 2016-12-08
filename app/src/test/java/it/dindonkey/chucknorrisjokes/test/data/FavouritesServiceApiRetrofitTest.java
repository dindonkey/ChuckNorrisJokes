package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import it.dindonkey.chucknorrisjokes.data.FavouriteOffer;
import it.dindonkey.chucknorrisjokes.data.FavouritesApiRetrofitFactory;
import it.dindonkey.chucknorrisjokes.data.FavouritesServiceApi;
import it.dindonkey.chucknorrisjokes.data.Offer;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by simonecaldon on 08/12/2016.
 */
public class FavouritesServiceApiRetrofitTest extends SharedTestCase
{

    private FavouritesServiceApi mFavouritesServiceApi;
    private TestSubscriber<List<FavouriteOffer>> mListFavouriteTestSubscriber;
    private TestSubscriber<FavouriteOffer> mFavouriteOfferTestSubscriber;


    @Before
    public void setUp() throws Exception
    {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        mFavouritesServiceApi = FavouritesApiRetrofitFactory.createService(mMockWebServer.url("/"));
        mListFavouriteTestSubscriber = new TestSubscriber<>();
        mFavouriteOfferTestSubscriber = new TestSubscriber<>();

    }

    @Test
    public void getFavouritesSimple() throws Exception
    {
        enqueueJsonHttpResponse("favourites_simple.json");

        mFavouritesServiceApi.getFavourites().subscribe(mListFavouriteTestSubscriber);
        mListFavouriteTestSubscriber.assertNoErrors();
        List<FavouriteOffer> favouriteOffers = mListFavouriteTestSubscriber.getOnNextEvents().get(0);

        assertEquals(1, favouriteOffers.size());
    }

    @Test
    public void getFavouritesComplex() throws Exception
    {
        enqueueJsonHttpResponse("favourites.json");

        mFavouritesServiceApi.getFavourites().subscribe(mListFavouriteTestSubscriber);
        mListFavouriteTestSubscriber.assertNoErrors();
        List<FavouriteOffer> favouriteOffers = mListFavouriteTestSubscriber.getOnNextEvents().get(0);

        assertEquals(1, favouriteOffers.size());
    }

    @Test
    public void addFavourite() throws Exception
    {
        FavouriteOffer favouriteOffer = new FavouriteOffer();
        favouriteOffer.favouriteId = "1234";
        Offer offer = new Offer();
        offer.id = "6666";
        favouriteOffer.offer = offer;

        mFavouritesServiceApi.addFavourite(favouriteOffer).subscribe(mFavouriteOfferTestSubscriber);
        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("{\"uuid\":\"ciccio@ciccio.it\",\"favouriteid\":\"1234\",\"offerid\":\"6666\"}",request.getBody().readUtf8());

    }
}