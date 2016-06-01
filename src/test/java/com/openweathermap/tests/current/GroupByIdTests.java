package com.openweathermap.tests.current;

import com.jayway.restassured.http.ContentType;
import com.openweathermap.City;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.jayway.restassured.RestAssured.given;
import static com.openweathermap.City.CITIES;
import static com.openweathermap.Common.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

//http://openweathermap.org/current#severalid
public class GroupByIdTests {
    public final String endpointURL = BASE_API_URL+"/group?appid="+API_KEY+"&";

    private ArrayList<City> arr = new ArrayList<City>();
    private ArrayList<Integer>  cityIds = new ArrayList<Integer>();
    private String cityIdsForRequest = "";


    @Before
    public void setUp()
    {
        Integer maxsize = new Random().nextInt(CITIES.size())+1;

        for (int i=0; i<maxsize; i++){
            arr.add(new City());   //add random cities to List.
        }

        for (City city: arr){
            cityIds.add(city.getCityId()); //Create List of IDs for expectation
            cityIdsForRequest += city.getCityId().toString()+","; //Create String of IDs for request param
        }
        cityIdsForRequest = cityIdsForRequest.substring(0, cityIdsForRequest.length()-1);
    }

    @After
    public void tearDown() {
        arr.clear();
        cityIds.clear();
        cityIdsForRequest = null;
    }

    @Test
    public void status200WhenCityIdsCorrect(){
        given().log().ifValidationFails().
                param("id", cityIdsForRequest).
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().statusCode(200);
    }

    /*Service doesn't support HTML and JSON for this method, but there isn't any mention about that in documentation:
    http://openweathermap.org/current#format */
    @Test
    public void checkResponseContentTypes(){
        //Verify all possible content types even with default (watch CONTENT_TYPES)
        for (Map.Entry<String, ContentType> entry: CONTENT_TYPES.entrySet()){
            given().log().ifValidationFails().
                    param("id", cityIdsForRequest).
                    param("mode", entry.getKey()).
            when().
                    get(endpointURL).
            then().
                    log().ifValidationFails().
                    assertThat().contentType(entry.getValue());
        }
    }

    @Test
    public void checkCityIdsPresentJSON(){
        given().log().ifValidationFails().
                param("id", cityIdsForRequest).
                param("mode", "json").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("list.id", equalTo(cityIds));
    }

    @Test
    public void countWeatherInResponseJSON(){
        given().log().ifValidationFails().
                param("id", cityIdsForRequest).
                param("mode", "json").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("list.weather.size()", equalTo(cityIds.size()));
    }

    @Test
    public void checkWeatherIsNotEmptyJSON(){
        given().log().ifValidationFails().
                param("id", cityIdsForRequest).
                param("mode", "json").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("list.weather.main[0]", not(empty()));
    }
}
