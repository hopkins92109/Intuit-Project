#include <bits/stdc++.h>
#include <grpcpp/grpcpp.h>
#include <iostream>
#include <string>
#include <fstream>

#include "personalinfo.grpc.pb.h"

using grpc::Server;
using grpc::ServerBuilder;
using grpc::ServerContext;
using grpc::Status;

using common::PersonalMessageReply;
using common::PersonalMessageRequest;
using common::PersonalInfoService;



struct PersonalInfoEntity {

    PersonalInfoEntity() {
       personalMessageRequest = NULL;
       nextNode = NULL;
    }

    const PersonalMessageRequest* personalMessageRequest;
    PersonalInfoEntity* nextNode;
};


class PersonalInfoDataBase {

  private:

    std::string filePath = "log.txt";

    PersonalInfoEntity* headNode;
    PersonalInfoEntity* currNode;

    void logTimeStamp(std::string time) {
       
        std::ofstream ofs(filePath.c_str(), std::ios_base::out | std::ios_base::app );
        ofs << "Received message: "  << time << '\n';
        ofs.close();
    }


  public:

      PersonalInfoDataBase() {
          headNode = NULL; 
          currNode = NULL;
      }

      bool addPersonalInfo(const PersonalMessageRequest* ptrPersonalMsgReq ) {

         if (headNode == NULL) {
            headNode = new PersonalInfoEntity();
            if (headNode == NULL) {
               return false;
            }
            currNode = headNode;
            currNode->personalMessageRequest = ptrPersonalMsgReq;
         } 
         else {
            currNode->nextNode = new PersonalInfoEntity();
            if (currNode->nextNode == NULL) {
               return false;
            }
            currNode = currNode->nextNode;
            currNode->personalMessageRequest = ptrPersonalMsgReq;
         }
          

         logTimeStamp( ptrPersonalMsgReq->timestamp() );
         return true;
     }

};


// Server Implementation
class ServiceImplementation final : public PersonalInfoService::Service {

   private: 
      PersonalInfoDataBase personalInfoDb;

   public:

      Status sendMessage(ServerContext* context, const PersonalMessageRequest* request,
                       PersonalMessageReply* reply) override {
       

        if (personalInfoDb.addPersonalInfo(request)) {
            reply->set_status(true);
        }
        else {
            reply->set_status(false);
        }

        return Status::OK;
     }
};



void RunServer() {

  std::string server_address("0.0.0.0:50051");
  ServiceImplementation service;
 
  ServerBuilder builder;
  // Listen on the given address without any authentication mechanism
  builder.AddListeningPort(server_address, grpc::InsecureServerCredentials());
  // Register "service" as the instance through which
  // communication with client takes place
  builder.RegisterService(&service);

  // Assembling the server
  std::unique_ptr<Server> server(builder.BuildAndStart());
  std::cout << "Server listening on port: " << server_address << std::endl;

  server->Wait();
}

int main(int argc, char** argv) {

  RunServer();

  return 0;
}




