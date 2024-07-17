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
    @DisplayName("Quando pegar todos os clientes sem cadastrar clientes, entao a lista deve estar vazia.")
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
    @DisplayName("Quando cadastrar novo cliente, entao ele deve estar disponível no resultado")
    public void cadastraCliente () {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": \"10029\",\n" +
                "  \"idade\": 27,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"10029\":{\"nome\":\"Mickey Mouse\",\"idade\":27,\"id\":10029,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoAPICliente+endpointCliente)
                .then()
                .statusCode(201).body(containsString(respostaEsperada));

    }

    @Test
    @DisplayName("Quando envio novos dados para o cliente, entao ele deve ser atualizado.")
    public void atualizaCliente () {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": \"10029\",\n" +
                "  \"idade\": 27,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String clienteAtualizado = "{\n" +
                "  \"id\": \"10029\",\n" +
                "  \"idade\": 28,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"10029\":{\"nome\":\"Mickey Mouse\",\"idade\":28,\"id\":10029,\"risco\":0}}";

        //Apenas cadastra o novo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoAPICliente+endpointCliente)
                .then().statusCode(201);

        //Atualizar o novo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteAtualizado)
                .when()
                .put(enderecoAPICliente+endpointCliente)
                .then()
                .statusCode(200).body(containsString(respostaEsperada));


    }
    @Test
    @DisplayName("Quando envio um DELETE para o usuario 10029, entao ele deve ser removido por ID.")
    public void deletarCliente () {
        String clienteParaCadastrar = "{\n" +
                "  \"id\": \"10029\",\n" +
                "  \"idade\": 27,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String clienteParaDeletar = "/10029"; //clientID
        String respostaDelecao = "CLIENTE REMOVIDO: { NOME: Mickey Mouse, IDADE: 27, ID: 10029 }";

        //Cadastra + Delete
        //Apenas cadastra o novo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoAPICliente+endpointCliente)
                .then().statusCode(201);

        // Faz o delete
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(enderecoAPICliente+endpointCliente+clienteParaDeletar)
                .then()
                .statusCode(200)
                .body(containsString(respostaDelecao));
    }

}