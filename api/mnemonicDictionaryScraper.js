const axios = require('axios');
const cheerio = require('cheerio');
const express = require('express');

const app = express();
const port = 3000;

app.get('/search', async (req,res) => {
  console.log("Invalid route use /search/:word to get data");
  res.status(404).send("Invalid Route")
})

app.get('/search/:word', async (req,res) => {

  const word = req.params.word;
  const url = 'https://mnemonicdictionary.com/word/' + word;

  const response = await axios.get(url);
  const $ = cheerio.load(response.data);

  let json = {"definitions" : [], "mnemonics" : []};
  
  json = getDefinitions( $ , json);
  json = getMnemonics( $ , json );
  res.send(json)

})

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`)
})


const getDefinitions = ($, json) => {

  const definitions = $('.media-body div').text().split('Definition');

  let definitonText = [];

  for(let i = 0; i < definitions.length; i++) {
    let definition = definitions[i].trim();

    if(definition.includes("word of the day")) {
      continue;
    }

    if(definition.includes('Synonyms')) {
        definition = definition.split('Synonyms')[0].trim();
    }

    if(definition.includes('Example Sentence')) {
      definition = definition.split('Example Sentence')[0].trim();
    }

    definitonText.push(definition);
  }

  if(definitonText.length == 0) {
      json["definitions"] = ["No Definitions Available"]
  }
  else json["definitions"] = definitonText

  return json;

}

const getMnemonics = ($, json) => {

  let mnemonics = [];

  let mnemonicsRes = $('.mnemonic-card').map((i, card) => {

    let cardText = $(card).find('.card-text p').text().trim();
    let cardLikes = $(card).find('.card-footer').text().trim().split(" ")[0];
    let cardDislikes = $(card).find('.card-footer').text().trim().split(" ")[3];

    let cardData = {
      "mnemonic" : cardText,
      "likes" : cardLikes,
      "dislikes" : cardDislikes
    }

    if(!cardText.includes("Powered by Mnemonic Dictionary")) mnemonics.push(cardData); 

  });

  if(mnemonics.length == 0) {
    mnemonics.push({
      "mnemonic" : "No Mnemonics Available",
      "likes" : -1,
      "dislikes" : -1
    });
  }
  json["mnemonics"] = mnemonics;

  return json;

}

