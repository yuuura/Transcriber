package com.yuuura.transcriber.service;

import com.google.gson.Gson;
import com.yuuura.transcriber.general.Transcript;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TranscriptService {

  @Getter
  private HttpResponse<String> postResponse;
  public Transcript getText(String api, String authorization){
    HttpRequest getRequest = HttpRequest.newBuilder()
        .uri(applyUriThrows(api))
        .header("Authorization", authorization)
        .build();

    HttpClient httpClient = HttpClient.newHttpClient();
    Transcript transcript;
    while(true){
      HttpResponse<String> getResponse = sendHttpRequest(httpClient, getRequest);
      transcript = new Gson().fromJson(getResponse.body(), Transcript.class);
      System.out.println(transcript.getStatus());
      if("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())){
        break;
      }
      sleep();
    }
    return transcript;
  }
  public Transcript sendAudio(String api, String authorization, Transcript transcript){

    Gson gson = new Gson();
    HttpRequest postRequest = HttpRequest.newBuilder()
        .uri(applyUriThrows(api))
        .header("Authorization", authorization)
        .POST(BodyPublishers.ofString(gson.toJson(transcript)))
        .build();

    HttpClient httpClient = HttpClient.newHttpClient();
    this.postResponse = sendHttpRequest(httpClient, postRequest);
    return gson.fromJson(postResponse.body(), Transcript.class);
  }

  private static HttpResponse<String> sendHttpRequest(HttpClient httpClient, HttpRequest httpRequest){
    try {
      return httpClient.send(httpRequest, BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  private static URI applyUriThrows(String uri){
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Wrong URI.", e);
    }
  }
  private static void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
