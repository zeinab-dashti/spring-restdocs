package com.zeinab.springrestdocs.controllers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class PersonControllerRestAssuredTest {

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void getPersonByIdShouldReturnOk() {
        RestAssured.defaultParser = Parser.JSON;

        RestAssured
                .given(this.spec)
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("persons/get-by-id",
                        pathParameters(parameterWithName("id")
                                .description("Identifier of the person to be obtained.")),
                        responseFields(
                                fieldWithPath("id")
                                        .description("Unique identifier of the person."),
                                fieldWithPath("firstName")
                                        .description("First Name of the person."),
                                fieldWithPath("lastName")
                                        .description("Last Name of the person."),
                                fieldWithPath("age")
                                        .description("Age of the person in years.")
                        )))
                .when().get("/persons/{id}",1)
                .then().assertThat().statusCode(is(200));
    }

}