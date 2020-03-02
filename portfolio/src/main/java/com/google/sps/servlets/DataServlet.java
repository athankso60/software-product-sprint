// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    //  private List<String> quotes;
     //private ArrayList<String> hard_coded_messages;
     

     @Override
  public void init() {
    //hard_coded_messages = new ArrayList<String>();
    //DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  }


     private String convertToJson(ArrayList<String> messages) {
        String json = "{";
         for(int i = 0 ; i< messages.size(); i++){
            if(i == messages.size()-1){
                json+= "\"message_"+ Integer.toString(i)+"\": ";
                json += "\"" + messages.get(i)+ "\"";
            }else{
                json+= "\"message_"+ Integer.toString(i)+"\": ";
                json += "\"" + messages.get(i)+ "\"";
                json += ", ";
            }
         }
        json += "}";
        
        return json;
    }

     

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
     // Convert messages to JSON
    //String json = convertToJson(hard_coded_messages);

    // Send the JSON as the response
    // response.setContentType("application/json;");
    // response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = getParameter(request, "text-input", "");
    //hard_coded_messages.add(text);
    

    Entity messageEntity = new Entity("Message");
    messageEntity.setProperty("text", text);
    

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(messageEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
