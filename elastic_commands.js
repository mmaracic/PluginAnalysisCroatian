POST _search
{
    "query": {
        "bool": {
            "should": [{
                  "match" : {
                      "streetName":"Bana Josipa Jelačića"
                  }},{
                  "match" : {
                      "streetNumber":10
                  }},{
                  "match" : {
                      "settlementName":"Zagreb"
                  }}
              ],
          "must": [{
                  "match" : {
                      "_type":"Address"
                  }}              
              ]
          }
    },
    "_source" : ["postalCode", "settlementName", "streetName", "streetNumber"]
}

POST _search
{
    "query": {
        "bool": {
            "should": [{
                  "match" : {
                      "streetName": {
                          "query" : "Šenoe",
                          "boost": 1
                          }
                  }},{
                  "match" : {
                      "streetNumber": {
                        "query":10,
                        "boost": 1
                      }
                  }},{
                  "match" : {
                      "settlementName":{
                          "query":"Zagreb",
                          "boost": 2
                      }
                  }}
              ],
          "must": [{
                  "match" : {
                      "_type":"Address"
                  }}              
              ]
          }
    },
    "_source" : ["postalCode", "settlementName", "streetName", "streetNumber", "countyName"]
}

POST _search
{
    "query": {
        "bool": {
            "must": [{
                  "match" : {
                      "_type":"Street"
                  }},{
                  "match" : {
                      "streetName": {
                          "query" : "Augusta Cesarac",
                          "boost": 1
                      }
                  }},{
                  "match" : {
                      "settlementName":{
                          "query":"Zagreb",
                          "boost": 1
                      }                  
                  }}
              ]
          }
    },
    "_source" : ["settlementName", "streetName", "countyName"]
}

POST _search
{
    "query": {
        "bool": {
            "must": [{
                  "match" : {
                      "_type":"Settlement"
                  }},{
                  "match" : {
                      "settlementName":"Zagreb"
                  }}
              ]
          }
    },
    "_source" : ["settlementName"]
}

POST _search
{
    "query": { 
  "bool" : {
    "should" : {
      "match" : {
        "streetName" : {
          "query" : "A.Starčevića",
          "type" : "boolean",
          "boost" : 1.0
        }
      }
    }
  }
}
}

GET _nodes/stats

POST _search
{
    "query":{
  "bool" : {
    "should" : [ {
      "match" : {
        "settlementName" : {
          "query" : "Šibenik",
          "type" : "boolean",
          "boost" : 1.0
        }
      }
    }, {
      "match" : {
        "id" : {
          "query" : 2001,
          "type" : "boolean",
          "boost" : 1.0
        }
      }
    }, {
      "match" : {
        "streetName" : {
          "query" : "Zapadna magistrala",
          "type" : "boolean",
          "boost" : 1.0
        }
      }
    }, {
      "match" : {
        "streetNumber" : {
          "query" : "9",
          "type" : "boolean",
          "boost" : 1.0
        }
      }
    } ]
  }     
    }
}

GET _nodes/plugins

GET _settings
POST ar/_close
POST ar/_open

DELETE ar
PUT ar


PUT ar/_settings
{
    "index" : {
        "analysis": {
            "tokenizer": {
                "FrontBackTokenizer": {
                    "type": "croatian_backfront_tokenizer",
                    "takeBack": 1,
                    "takeFront": 3
                }
            },
            "analyzer": {
                "cro_analyzer" : {
                    "type": "custom",
                    "char_filter": [],
                    "tokenizer": "FrontBackTokenizer",
                    "filter": []
                },
                "text_number_analyzer" : {
                    "type": "custom",
                    "char_filter": [],
                    "tokenizer": "croatian_number_tokenizer",
                    "filter": []
                }
            }
        }
    }
}


PUT /ar/_mapping/test
{
    "properties": {
        "text": {
            "type":      "string",
            "analyzer":  "cro_analyzer"
        }
    }
}

GET /ar/_mapping
DELETE /ar/_mapping/number
PUT /ar/_mapping/number
{
    "properties": {
        "number": {
            "type":      "string",
            "analyzer":  "text_number_analyzer"
        }
    }
}

POST /_analyze
{
  "tokenizer" : "croatian_number_tokenizer",
  "text" : "This is a 1996 test threading"
}

POST /_analyze
{
  "tokenizer" : "croatian_backfront_tokenizer",
  "text" : "This is a 1996 test threading"
}

POST /ar/test/
{
    "text": "This is a 1996 test threading"
}

POST /ar/number/
{
    "number": "year 1996"
}

POST /ar/number/
{
    "number": 1996
}

POST _search
{
    "query" : {
        "match_all" : {}
    }
}

POST _search
{
    "query" : {
        "match" : {
            "text": {
                "query" : "treaded",
                "fuzziness": "AUTO"
            }
        }
    }
}

POST _search
{
    "query" : {
        "match" : {
            "number": {
                "query" : 1996
            }
        }
    }
}