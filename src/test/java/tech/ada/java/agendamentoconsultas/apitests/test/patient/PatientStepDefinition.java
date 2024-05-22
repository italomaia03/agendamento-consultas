package tech.ada.java.agendamentoconsultas.apitests.test.patient;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;

public class PatientStepDefinition {
    private RequestSpecification request = RestAssured.given()
        .baseUri("http://localhost:8080")
        .contentType(ContentType.JSON);
    private Response response = null;
    private PatientDtoRequest patient = null;
    private PatientUpdateRequestDto updateDto = null;
    private String token;
    private String uuid;
    private String validEmail = "";

    @Given("paciente válido e não cadastrado")
    public void validPatientNotSignIn(){
        patient = new PatientDtoRequest(
            "Test",
            "test@testing.com",
            "Senha_da_Nasa_123",
            "(82) 99999-9999",
            "76530527036",
            new AddressRequestDto(
                "27259-230",
                0
            ));
    }

    @When("cadastrar o paciente")
    public void signInNewPatient(){
        var response = request.body(patient).when().post("/api/v1/auth/sign-up");
        uuid = response.jsonPath().getString("uuid");
    }

    @Then("paciente consegue logar na plataforma")
    public void loginPatient() {
        LoginRequestDto login = new LoginRequestDto("test@testing.com", "Senha_da_Nasa_123");
        var response = request.body(login).when().post("/api/v1/auth/login");
        response.then().statusCode(200);
    }

    @Given("email de um paciente válido")
    public void givenValidEmail() {
        validEmail = "ana.silva@example.com";
    }

    @When("o administrador estiver logado")
    public void loginWithAdmin() {
        LoginRequestDto login = new LoginRequestDto("admin@example.com", "Senha_forte123"); 
        var response = request.body(login).when().post("/api/v1/auth/login");
        JsonPath jsonPath = response.jsonPath();
        token = jsonPath.getString("token");
    }

    @Then("deve retorna o paciente com o email")
    public void searchPatient(){
        request = RestAssured.given()
        .baseUri("http://localhost:8080")
        .contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + token);

        var response = request.when().get("/api/v1/patients/"+validEmail);
        Assertions.assertEquals("Ana Silva", response.jsonPath().getString("nome"));
    }

    @Given("paciente com um cep não rastreado")
    public void notValidPatientCep() {
        patient = new PatientDtoRequest(
            "Test",
            "test1@testing.com",
            "Senha_da_Nasa_123",
            "(82) 99999-9999",
            "30270283080",
            new AddressRequestDto(
                "00000-000",
                0
            ));
    }

    @When("Cadastrar paciente com cep não rastreado")
    public void signInnotValidPatient() {
        response = request.body(patient).when().post("/api/v1/auth/sign-up");
    }

    @And("Deve retornar um erro {int}")
    public void patientNotSignIn(Integer error) {
        response.then().statusCode(error);
    }
}
