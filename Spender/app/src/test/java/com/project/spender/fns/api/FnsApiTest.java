package com.project.spender.fns.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import okhttp3.Credentials;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FnsApiTest {
    private Retrofit retrofit;
    private FnsApi fns;
    private String loginPassword;
    private String fn;
    private String fd;
    private String fiscalSign;
    private String date;
    private String sum;

    @BeforeEach
    void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://proverkacheka.nalog.ru:9999") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()).build(); //Конвертер, необходимый для преобразования JSON'а в объекты
        fns =retrofit.create(FnsApi.class);
        loginPassword = Credentials.basic("+79112813247","882107");
        fn = "9286000100242530";
        fd = "27641";
        fiscalSign = "124643923";
        date = "20190402T1357";
        sum = "21700";
    }

    @Test
    void isCheckExist() throws IOException {
        fns.login(loginPassword).execute();
        Response res = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        System.out.println(res.code());
        System.out.println(res.message());
        assertTrue(res.isSuccessful());
    }

    @Test
    void login() throws IOException {
        Response res = fns.login(loginPassword).execute();
        System.out.println(res.code());
        System.out.println(res.message());
        assertTrue(res.isSuccessful());
    }

    @Test
    void getCheck() throws IOException {
        Response resExist = fns.isCheckExist(fn, fd, fiscalSign, date, sum).execute();
        System.out.println(resExist.code());
        System.out.println(resExist.message());

        Response res = fns.getCheck(loginPassword, "", "", fn, fd, fiscalSign, "no").execute();
        System.out.println(res.code());
        System.out.println(res.message());
        assertTrue(res.isSuccessful());
    }
}