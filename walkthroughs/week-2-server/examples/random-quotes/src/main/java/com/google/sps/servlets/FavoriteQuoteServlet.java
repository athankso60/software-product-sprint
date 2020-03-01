/*Practice serverlet*/
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Test Serverlet that returns favorite quote */
@WebServlet("/favorite-quote")
public final class FavoriteQuoteServlet extends HttpServlet {

  

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String favQuote = "Great spirits have always encountered opposition from mediocre minds. The mediocre mind is incapable of understanding the man who refuses to bow blindly to conventional prejudices and chooses instead to express his opinions courageously and honestly.-Albert Einsten";

    response.setContentType("text/html;");
    response.getWriter().println(favQuote);
  }
}
