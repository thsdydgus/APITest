package com.skcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AwsTest {

    public static void main(String[] args) throws IOException {
        String AWS_BUCKETNAME = "test";
        String AWS_ACCESS_KEY = "test";
        String AWS_SECRET_KEY = "your_ test";
        String file_path = "test/;"; // 폴더명
        String file_name = "test.txt"; // 파일명 

        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentials);
       
        try {
        	// 파일 업로드 부분 파일 이름과 경로를 동시에 넣어줌.
        	 PutObjectRequest putObjectRequest = new PutObjectRequest(AWS_BUCKETNAME, file_path+file_name, createSampleFile());
             putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); // URL 접근시 권한 읽을수 있도록 설정.
             s3.putObject(putObjectRequest);
             System.out.println("Uploadinf OK");
             
             // 파일 다운로드 다운로드 경로와 파일이름 동시 필요. 
             System.out.println("Downloading an object");
             S3Object object = s3.getObject(new GetObjectRequest(AWS_BUCKETNAME, file_path+file_name));
             System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
             displayTextInputStream(object.getObjectContent());
             
             
             
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("파일테스트하기\n");
        writer.close();

        return file;
    }

    @SuppressWarnings("unused")
	private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }
}
