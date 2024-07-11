import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TestaCliente {

    String enderecoAPICliente = "http://localhost:8080/";
    String endpointCliente = "cliente";

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

    @Test
    @DisplayName("Quando cadastrar novo cliente, então ele deve estar disponível no resultado")
    public void cadastraCliente () {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": \"4055696\",\n" +
                "  \"idade\": 27,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"4055696\":{\"nome\":\"Mickey Mouse\",\"idade\":27,\"id\":4055696,\"risco\":0}}";

        given().contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when().post(enderecoAPICliente+endpointCliente)
                .then().statusCode(201).body(containsString(respostaEsperada));

    }

    @Test
    @DisplayName("Quando envio novos dados para o usuario cadastrado, então ele deve ser atualizado.")
    public void atualizarCliente () {
        String clienteParaAtualizar = "{\n" +
                "  \"id\": \"4055696\",\n" +
                "  \"idade\": 27,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaAtualizada = "{\"4055696\":{\"nome\":\"Mickey Mouse\",\"idade\":27,\"id\":4055696,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaAtualizar)
                .when().put(enderecoAPICliente+endpointCliente)
                .then().statusCode(200).body(containsString(respostaAtualizada));

    }
    @Test
    @DisplayName("Quando envio um DELETE para o usuario 4055696, entao ele deve ser removido. ")
    public void deletarCliente () {
        String clienteParaDeletar = "/4055696"; //clientID
        String respostaDelecao = "CLIENTE REMOVIDO: { NOME: Mickey Mouse, IDADE: 27, ID: 4055696 }";

        given()
                .contentType(ContentType.JSON)
                .when().delete(enderecoAPICliente+endpointCliente+clienteParaDeletar)
                .then().statusCode(200).body(containsString(respostaDelecao));
    }

}
