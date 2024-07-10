import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestaCliente {

    String enderecoAPICliente = "http://localhost:8080/";

    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar clientes, então a lista deve estar vazia.")
    public void pegaTodosClientes () {

        String respostaEsperada = "{}";

        given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(enderecoAPICliente)
                .then()
                    .statusCode(200)
                    .assertThat().body(new IsEqual<>(respostaEsperada));
    }
}
