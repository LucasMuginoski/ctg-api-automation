import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestaCliente {

    private static final String servicoAPI = "http://localhost:8080";
    private static final String recursoCliente = "/cliente";
    private static final String apagaTodos = "/apagaTodos";
    private static final String risco = "/risco/";
    private static final String listaClientesVazia = "{}";

    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar clientes, entao a lista deve estar vazia.")
    public void QuandoRequestListaSemClientes_EntaoDeveEstarVazia () {

        apagaTodosClientesDoServidor();
        pegarTodosClientes()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(listaClientesVazia));

    }

    @Test
    @DisplayName("Quando cadastrar novo cliente, entao ele deve estar disponível no resultado")
    public void QuandoCadastraNovoCliente_EntaoEleDeveEstarDisponivel () {

        Cliente clienteParaCadastrar = new Cliente();

        clienteParaCadastrar.setId("10029");
        clienteParaCadastrar.setNome("Lucas");
        clienteParaCadastrar.setIdade(28);
        clienteParaCadastrar.getRisco(0);

        postaCliente(clienteParaCadastrar)
                .statusCode(HttpStatus.SC_CREATED)
                .body("10029.nome", equalTo("Lucas"))
                .body("10029.idade", equalTo(28))
                .body("10029.id", equalTo(10029))
                .body("10029.risco", equalTo(0));

    }

    @Test
    @DisplayName("Quando envio novos dados para o cliente, entao ele deve ser atualizado.")
    public void QuandoEnvioNovosDados_EntaoClienteDeveSerAtualizadoComSucesso () {

        Cliente clienteParaCadastrar = new Cliente();

        clienteParaCadastrar.setNome("Mickey, Mouse");
        clienteParaCadastrar.setIdade(67);
        clienteParaCadastrar.setId("40101");
        postaCliente(clienteParaCadastrar);

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Mickey, Mouse");
        clienteAtualizado.setIdade(85);
        clienteAtualizado.setId("40101");

        atualizaCliente(clienteAtualizado)
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("40101.id", equalTo(40101))
                .body("40101.nome", equalTo("Mickey, Mouse"))
                .body("40101.idade", equalTo(85));


    }

    @Test
    @DisplayName("Quando deletar cliente, entao ele deve ser removido com sucesso")
    public void quandoDeletarCliente_EntaoUsuarioDeveSerRemovidoComSucesso () {

        // Arrange
        Cliente cliente = new Cliente();

        //act
        cliente.setNome("Tio Patinhas");
        cliente.setIdade(89);
        cliente.setId("40102");
        postaCliente(cliente);

        //Act / Assert

        apagaCliente(cliente)
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(not(contains("Tio Patinhas")));


    }

    @Test
    @DisplayName("Quando solicitar risco de um cliente com credenciais validas, entao ele deve ser retornado com sucesso")
    public void quandoSolicitarRiscoComAutorizacao_EntaoDeveRetornarRisco(){
        Cliente cliente = new Cliente("Mickey Mouse", 32, "220389",-50);
        int riscoEsperado = -50;

        postaCliente(cliente);
        given()
                .auth()
                .basic("aluno", "senha")
        .when()
                .get(servicoAPI+risco+cliente.getId())
        .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("risco", equalTo(riscoEsperado));

    }


    private ValidatableResponse pegarTodosClientes(){
        return given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(servicoAPI)
                .then();

    }

    private ValidatableResponse postaCliente(Cliente clienteParaPostar){
        return given()
                    .contentType(ContentType.JSON)
                    .body(clienteParaPostar)
                .when()
                    .post(servicoAPI+recursoCliente)
                .then();
    }

    private ValidatableResponse atualizaCliente (Cliente clienteParaAtualizar){
        return given()
                .contentType(ContentType.JSON)
                .body(clienteParaAtualizar).
                when().
                put(servicoAPI + recursoCliente).
                then();
    }

    private  ValidatableResponse apagaCliente (Cliente clienteParaDeletar){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(servicoAPI+recursoCliente+"/"+ clienteParaDeletar.getId())
                .then();
    }

    @AfterEach
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