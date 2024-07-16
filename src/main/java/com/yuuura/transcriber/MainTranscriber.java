package com.yuuura.transcriber;

import com.yuuura.transcriber.general.Transcript;
import com.yuuura.transcriber.service.TranscriptService;
import java.util.Optional;

public class MainTranscriber {

  public static void main(String[] args) {

    Transcript transcript = new Transcript();
    // Documentation to endpoint: https://www.assemblyai.com/docs/api-reference/transcripts/submit
    String audio_url = "https://www.computerhope.com/jargon/m/example.mp3";
    transcript.setAudio_url(audio_url);

    TranscriptService transcriptService = new TranscriptService();
    String trans_base_url = "https://api.assemblyai.com/v2/transcript";
    transcript = transcriptService.sendAudio(trans_base_url, args[0], transcript);

    System.out.println(Optional.ofNullable(transcriptService.getPostResponse().body()).orElse("No response body."));

    transcript = transcriptService.getText(trans_base_url + "/" + transcript.getId(), args[0]);

    System.out.println("Transcription completed!");
    System.out.println(transcript.getText());
  }

}
