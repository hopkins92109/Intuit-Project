package com.personalinfo.service.impl;

import com.personalinfo.dto.PersonalInfoDto;
import com.personalinfo.service.PersonalService;
import io.grpc.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import io.grpc.personal.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
public class PersonalServiceImpl extends PersonalInfoServiceGrpc.PersonalInfoServiceImplBase implements PersonalService {
   private final static String SERVER_ADDRESS = "0.0.0.0:50051";
   private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public boolean sendPersonalInfo(PersonalInfoDto personalInfoDto) {

        try {

          return sendMessageToServer(personalInfoDto);
        }
        catch (InterruptedException e) {
            return false;
        }
    }

  public boolean sendMessageToServer(PersonalInfoDto info) throws InterruptedException {
      PersonalMessageRequest request = PersonalMessageRequest.newBuilder().setFirstName(info.getFirstName())
              .setLastName(info.getLastName())
              .setDob(info.getDateOfBirth())
              .setEmail(info.getEmail())
              .setPhone(info.getPhoneNumber())
              .setTimeStamp(getCurrentTimeStamp())
              .build();

      ManagedChannel channel = Grpc.newChannelBuilder(SERVER_ADDRESS, InsecureChannelCredentials.create()).build();

      PersonalInfoServiceGrpc.PersonalInfoServiceBlockingStub blockingStub = PersonalInfoServiceGrpc.newBlockingStub(channel);

      PersonalMessageReply response;
      try {
          response = blockingStub.sendMessage(request);
      }
      catch (StatusRuntimeException ex) {
         System.out.println(ex.toString());
         ex.printStackTrace();
         return false;
      }
      finally {
          // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
          // resources the channel should be shut down when it will no longer be used. If it may be used
          // again leave it running.
          channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      }

      return response.getStatus();
  }

  private String getCurrentTimeStamp() {
      Date date = Calendar.getInstance().getTime();
      String strDate = DATE_FORMAT.format(date);
      return strDate;
  }

}
