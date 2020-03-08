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

/*Hide comment form*/


checkWhetherUserIsLoggedIn();

//

/**
 * Adds a random greeting to the page.
 */

function addRandomQuote() {
//add random quotes I love
  const randomQuote =
      [' "Knowing is not enough we must apply, Willing is not enough we must do -Bruce Lee"', 
      ' "Great Spirits have always encountered violient opposition from mediocre minds - Albert Einstein"',
       '"Your work is going to fill a large part of your life, and the only way to be truly satisfied is to do what you believe is great work. And the only way to do great work is to love what you do. If you haven\'t found it yet, keep looking. Don\'t settle." - Steve Jobs', 
       '"Fear can hold you prisoner, Hope can set you free." - Shawshank Redemption'];

  // Pick a random greeting.
  const quote = randomQuote[Math.floor(Math.random() * randomQuote.length)];

  // Add it to the page.
  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}


function getMessages() {
  fetch('/data').then(response => response.json()).then((messages) => {
    
    console.log(messages);

    const commentList = document.getElementById('message-container');
    commentList.innerHTML = '';
    for( let element in messages ){
        var node = createListElement(messages[element]);
        console.log(node);
        commentList.appendChild(node);
    }
    
  });
}



//check whether user is logged in using async
async function checkWhetherUserIsLoggedIn() {
    const commentForm = document.getElementById('comment-form');
    const logInOutDiv = document.getElementById('logInOut');
  //set visibility of form and logurl to none
   commentForm.style.display="none";
   logInOutDiv.style.display="none";
  
  //get login status
  const response = await fetch('/login');
  const loginInfo = await response.json();
  console.log(loginInfo);

  //convert login information to json object
//   var obj = JSON.parse(loginInfo);
//   console.log(obj.isLoggedIn);
//   console.log(obj.logUrl);

  //display form and logUrl based on whether user is logged in or not
  if(loginInfo.isLoggedIn === "true"){
      
      commentForm.style.display="block";
      logInOutDiv.innerHTML = '';
      logInOutDiv.appendChild(createLinkElement("Logout here",loginInfo.logUrl));
      logInOutDiv.style.display="block";
  }else{
      logInOutDiv.innerHTML = '';
      logInOutDiv.appendChild(createLinkElement("Login here to add comments",loginInfo.logUrl));
      logInOutDiv.style.display="block";
  }
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

/*Creates a <a> element containing text */
function createLinkElement(text,link) {
  const linkElement = document.createElement('a');
  linkElement.appendChild(document.createTextNode(text));
  linkElement.title = text;
  linkElement.href = link;
  return linkElement;
}



