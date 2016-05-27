package com.openweathermap.tests.current;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.openweathermap.Common.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.http.ContentType;

//http://openweathermap.org/current#cityid
public class ByCityId {
    public final String endpointURL = BASE_API_URL+"/weather?appid="+API_KEY+"&";

    private String cityName;
    private String countyCode;
    private Integer cityId;

    //You can change to @BeforeClass if needed.
    @Before
    public void setUp()
    {
        HashMap<String, Object> city = getRandomCity();
        cityName = getCityNameFromMap(city);
        countyCode = getCountryCodeFromMap(city);
        cityId = getCityIdFromMap(city);
    }

    @After
    public void tearDown() {
        cityName = null;
        countyCode = null;
        cityId = null;
    }

    @Test
    public void status200WhenCityIdCorrect(){
        given().
                param("id", cityId).
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().statusCode(200);
    }

    @Test
    public void checkResponseContentTypes(){
        //Verify all possible content types even with default (watch CONTENT_TYPES)
        for (Map.Entry<String, ContentType> entry: CONTENT_TYPES.entrySet()){
            given().
                    param("id", cityId).
                    param("mode", entry.getKey()).
            when().
                    get(endpointURL).
            then().
                    log().ifValidationFails().
                    assertThat().contentType(entry.getValue());
        }
    }

    @Test
    public void status404WhenCityIdIncorrect(){
        given().
                param("id", randomString()).
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().statusCode(404);
    }

    @Test
    public void checkBodyMessageWhenCityIdIncorrect(){
        given().
                param("id", randomString()).
                param("mode", "json").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("cod", equalTo("404")).and().
                body("message", equalTo("Error: Not found city"));
    }

    @Test
    public void checkCityIdAndNameInJSON(){
        given().
                param("id", cityId).
                param("mode", "json").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("name", equalTo(cityName)).and().
                body("id", equalTo(cityId)).and().
                body("sys.country", equalTo(countyCode));
    }

    @Test
    public void checkCityIdAndNameInXML(){
        given().
                param("id", cityId).
                param("mode", "xml").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("current.city.@name", equalTo(cityName)).and().
                body("current.city.@id", equalTo(cityId.toString())).and().
                body("current.city.country", equalTo(countyCode));
    }

    @Test
    public void checkWeatherIsNotEmptyInJSON(){
        given().
                param("id", cityId).
                param("mode", "json").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("main.temp", not(empty())).and().
                body("weather.description", not(empty()));
    }

    @Test
    public void checkWeatherIsNotEmptyInXML(){
        given().
                param("id", cityId).
                param("mode", "xml").
        when().
                get(endpointURL).
        then().
                log().ifValidationFails().
                assertThat().body("current.temperature.@value ", not(empty())).and().
                body("current.clouds.@value ", not(empty()));
    }
}
