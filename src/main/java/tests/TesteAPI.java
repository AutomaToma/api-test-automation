package tests;

import core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteAPI extends BaseTest {

    String token;
    static int usuarioId;
    static int contaId;
    static String nomeConta;

    @Before
    public void deveRealizarOLogin() {

        Map<String, String> body = new HashMap<>();

        body.put("email", "pauloqfs16@gmail.com");
        body.put("senha", "Paulo010203@");

        token = RestAssured.given()
                .body(body)
                .when()
                .log().all()
                .post("/signin")
                .then()
                .log().all()
                .statusCode(200)
                .body("nome", Matchers.is("Paulo Quintino"))
                .extract().path("token");

        System.out.println("=========================================================================================================");
        System.out.println(token);
        System.out.println("=========================================================================================================");
    }


    @Test
    public void deveCadastrarConta() {
        Random random = new Random();

        nomeConta = "Conta " + random.nextInt(50);

        Map<String, String> body = new HashMap<>();
        body.put("nome", nomeConta);

        contaId = RestAssured.given()
                .header("Authorization", "JWT " + token)
                .body(body)
                .log().all()
                .when()
                .post("/contas")
                .then()
                .log().all()
                .statusCode(201)
                .body("nome", Matchers.is(nomeConta))
                .extract().path("id");

        System.out.println("===================================");
        System.out.println(contaId);
        System.out.println("===================================");
    }

    @Test
    public void deveRetornarContaCriada() {

        RestAssured.given()
                .header("Authorization", "JWT " + token)
                .when()
                .log().all()
                .get("/contas/" + contaId)
                .then()
                .statusCode(200)
                .body("id", Matchers.is(contaId))
                .body("nome", Matchers.is(nomeConta))
                .body("visivel", Matchers.equalTo(true));
    }

    @Test
    public void deveRetornarTodasAsContas() {
        Response response = RestAssured.given()
                .header("Authorization", "JWT " + token)
                .when()
                .log().all()
                .get("/contas");

        int tamanho = response.body().jsonPath().getList("").size();

        System.out.println("==============================");
        System.out.println("TAMANHO DA LISTAGEM " + tamanho);
        System.out.println("==============================");

        response.then()
                .statusCode(200)
                .log().all()
                .body("id", Matchers.hasSize(Matchers.greaterThan(0)))
                .body("id", Matchers.notNullValue())
                .body("nome", Matchers.hasItem("Conta 5"))
                .body("", Matchers.hasSize(tamanho));
    }


}
