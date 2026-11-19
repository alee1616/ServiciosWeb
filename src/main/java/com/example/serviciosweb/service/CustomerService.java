package com.example.serviciosweb.service;

import com.example.serviciosweb.model.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Customer> obtenerClientes() throws IOException, InterruptedException {

        // ni la URL ni la llave están en el código se leen de variables de entorno
        String url = System.getenv("SUPABASE_URL");
        String key = System.getenv("SUPABASE_KEY");

        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Falta la variable de entorno SUPABASE_URL");
        }
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("Falta la variable de entorno SUPABASE_KEY");
        }

        // es la misma petición que se hace en Postman get con el header apikey
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", key)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("La API respondió con código " + response.statusCode());
        }

        // el JSON es un arreglo se recorre y se crea un customer por cada objeto
        JsonNode arreglo = mapper.readTree(response.body());
        List<Customer> clientes = new ArrayList<>();

        for (JsonNode c : arreglo) {
            clientes.add(new Customer(
                    c.path("id").asInt(),
                    c.path("first_name").asText(),
                    c.path("last_name").asText(),
                    c.path("email").asText(),
                    c.path("phone").asText(),
                    c.path("date_of_birth").asText(),
                    c.path("country").asText(),
                    c.path("city").asText(),
                    c.path("address").asText(),
                    c.path("annual_income").asDouble(),
                    c.path("credit_score").asInt(),
                    c.path("has_unpaid_debt").asBoolean(),
                    c.path("unpaid_debt_amount").asDouble(),
                    c.path("status").asText(),
                    c.path("created_at").asText(),
                    c.path("updated_at").asText()
            ));
        }
        return clientes;
    }
}