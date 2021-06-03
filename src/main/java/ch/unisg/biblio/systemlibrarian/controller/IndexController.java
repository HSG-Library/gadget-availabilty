package ch.unisg.biblio.systemlibrarian.controller;

import java.util.Map;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

@Controller("/")
public class IndexController {

    @View("index")
    @Get
    public HttpResponse<Map<String, Object>> index() {
        return HttpResponse.ok(Map.of());
    }

    @View("info")
    @Get("/info")
    public HttpResponse<Map<String, Object>> info() {
        return HttpResponse.ok(Map.of());
    }
}
