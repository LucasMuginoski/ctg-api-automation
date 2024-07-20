import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestaCliente {

    private String servicoAPI = "http://localhost:8080";
    private String recursoCliente = "/cliente";
    private String apagaTodos = "/apagaTodos";
    private static final String listaClientesVazia = "{}";

    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar clientes, entao a lista deve estar vazia.")
    public void pegaTodosClientes () {

        apagaTodosClientesDoServidor();

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(servicoAPI)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(listaClientesVazia));
    }

    @Test
    @DisplayName("Quando cadastrar novo cliente, entao ele deve estar disponível no resultado")
    public void cadastraCliente () {

        apagaTodosClientesDoServidor();
        Cliente clienteParaCadastrar = new Cliente();

        clienteParaCadastrar.setId("10029");
        clienteParaCadastrar.setNome("Lucas");
        clienteParaCadastrar.setIdade(28);
        clienteParaCadastrar.getRisco(0);


        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(servicoAPI+recursoCliente)
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("10029.nome", equalTo("Lucas"))
                .body("10029.idade", equalTo(28))
                .body("10029.id", equalTo(10029))
                .body("10029.risco", equalTo(0));

    }

    @Test
    @DisplayName("Quando envio novos dados para o cliente, entao ele deve ser atualizado.")
    public void atualizaCliente () {

        Cliente clienteParaCadastrar = new Cliente();

        clienteParaCadastrar.setNome("Mickey");
        clienteParaCadastrar.setIdade(67);
        clienteParaCadastrar.setId("40101");

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Mickey, Mouse");
        clienteAtualizado.setIdade(85);
        clienteAtualizado.setId("40101");


        //Apenas cadastra o novo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(servicoAPI+recursoCliente)
         .then()
                .statusCode(HttpStatus.SC_CREATED);

        //Atualizar o novo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteAtualizado)
        .when()
                .put(servicoAPI+recursoCliente)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("40101.id", equalTo(40101))
                .body("40101.nome", equalTo("Mickey, Mouse"))
                .body("40101.idade", equalTo(85));


    }

    @Test
    @DisplayName("Quando envio um DELETE para o usuario 10029, entao ele deve ser removido por ID.")
    public void deletarCliente () {

        //garantir q não existe lixo no servidor
        apagaTodosClientesDoServidor();

        Cliente clienteParaCadastrar = new Cliente();

        clienteParaCadastrar.setNome("Tio Patinhas");
        clienteParaCadastrar.setIdade(89);
        clienteParaCadastrar.setId("40102");

        //Cria usuário para testar a deleção
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(servicoAPI+recursoCliente)
        .then()
                .statusCode(HttpStatus.SC_CREATED);

        //Aqui é o método do teste
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(servicoAPI+recursoCliente+"/"+clienteParaCadastrar.getId())
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(not(contains("Tio Patinhas")));


    }


    public void apagaTodosClientesDoServidor(){
        String respostaEsperada = "{}";
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(servicoAPI+recursoCliente+apagaTodos)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(respostaEsperada));

    }
}