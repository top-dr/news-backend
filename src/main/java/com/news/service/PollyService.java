package com.news.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.news.model.NewsVO;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyAsyncClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;
import software.amazon.awssdk.services.polly.model.Voice;
import software.amazon.awssdk.services.polly.model.VoiceId;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Component
public class PollyService {
	
	private static final String S3_BUCKET_NAME = "ndr-talking-app";

	private static final Logger log = LoggerFactory.getLogger(PollyService.class);
	
	private static final String baseUrl = "https://ndr-talking-app.s3.eu-west-3.amazonaws.com/";
	
	private  PollyAsyncClient polly = null;
			
	PollyService() {
		
	}
	
	public void toPolly(NewsVO news) {
		
		log.info("************ Polly Call BEGIN *************");
		
		// create an Amazon Polly client in a specific region
		polly = PollyAsyncClient.builder().credentialsProvider(DefaultCredentialsProvider.create())
				.region(Region.EU_WEST_3).build();
		
		String article = news.getContent();
		
		if(article.length() > 3000) {
			article =  article.substring(0, 3000);
		}
		
		Whitelist wiList = new Whitelist();		
		String text = Jsoup.clean(article, wiList);
		
		String url = "";
		
		try {
			
			this.synthesize(text, news.getVoiceUrl());
			
		} catch (Exception e) {
			log.error(" Erreur synthese vocal Polly : " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("************ Polly Call END *************");
				
	}
	
	private void synthesize(String text, String urlS3) throws IOException {
		
		SynthesizeSpeechRequest synthReq = SynthesizeSpeechRequest.builder().text(text)
				.voiceId(VoiceId.ZEINA).outputFormat(OutputFormat.MP3).build();
		
		
		CompletableFuture<ResponseBytes<SynthesizeSpeechResponse>> 
		response = polly.synthesizeSpeech(synthReq, 
				AsyncResponseTransformer.toBytes());
		
		response.whenComplete((resp , err) -> {
			try {
                if (resp != null) {
                	//ResponseBytes<SynthesizeSpeechResponse> res = (ResponseBytes<SynthesizeSpeechResponse>) resp;
                	
                	
                	this.putS3Async(resp, urlS3);
                    
                } else {
                    // Handle error
                    err.printStackTrace();
                }
            }finally {
            	polly.close();
			} 
		});
		
		response.join();
	}
	
	public void putS3Async(ResponseBytes<SynthesizeSpeechResponse> bytes, String urlWithoutBase) {
		
		Region region = Region.EU_WEST_3;
		S3AsyncClient client = S3AsyncClient.builder().region(region).build();
        CompletableFuture<PutObjectResponse> future = client.putObject(
                PutObjectRequest.builder()
                                .bucket(S3_BUCKET_NAME)
                                .key(urlWithoutBase)
                                .build(),
                AsyncRequestBody.fromByteBuffer(bytes.asByteBuffer()));
        
        future.whenComplete((resp, err) -> {
            try {
                if (resp != null) {
                    System.out.println("my response: " + resp);
                } else {
                    // Handle error
                    err.printStackTrace();
                }
            } finally {
                // Lets the application shut down. Only close the client when you are completely done with it.
                client.close();
            }
        });

        future.join();
	}

}
