PUT movies/_doc/1924
{
  "id": "1924", "title" : "Plan 9 from Outer Space", "year":1959 , "genre":["Horror", "Sci-Fi"]
}



#PUT movies/_doc/1
{
  "genre": ["IMAX", "Sci-Fi"],
  "title": "Intersteller",
  "year": 2014

}


#DELETE movies


PUT movies
{
  "mappings": {
      "movie":{
  			"properties": {
  				"year": {
  					"type":"date"
  				}
  			}
		}
	}
}

GET movies/_search

GET movies/_search
{
  "query":{
    "match":{
      "title":"Star Wars: Episode VII - The Force Awakens"
    }
  }
}

GET movies/_search
{
  "query":{
    "match_phrase":{
      "genre":"Horror"
    }
  }
}

#PUT movies

#DELETE movies
#DELETE mymovies

PUT mymovies
{
  "mappings": {
    "properties": {
      "id":{
        "type":"integer"
      },
      "year":{
        "type":"date"
      },
      "genre":{
        "type":"keyword"
      },
      "title":{
        "type": "text", "analyzer": "english"
      }
    }
  }
}

PUT mymovies/_doc/2019
{
   
  "id": "12345", "title" : "Uri", "year":2019 , "genre":["War", "Love"]
}

PUT mymovies/_doc/2018
{
   
  "id": "10045", "title" : "Indian Culture", "year":2018 , "genre":["Culture"]
}

GET mymovies/_search

GET mymovies/_search
{
  "query":{
    "match":{
      "title":"Star Wars: Episode VII - The Force Awakens"
    }
  }
}

GET mymovies/_search
{
  "query":{
    "match_phrase":{
      "genre":"War"
    }
  }
}

GET shakespeare/current/_search

#DELETE shakespeare

#DELETE series

PUT series
{
	"mappings":{
		"movie":{
			"properties":{
				"film_to_franchise":{
					"type": "join",
					"relations":{
						"franchise":"film"
					}
				}
			}
		}
	}
}

GET series/movie/_search

GET series/movie/_search
{
  "query":{
    "has_parent":{
      "parent_type": "franchise",
      "query": {
        "match": {
          "title": "Star Wars"
        }
      }
    }
  }
}

GET series/movie/_search
{
  "query":{
    "has_child": {
      "type": "film",
      "query": {
        "match": {
          "title": "The Force Awakens"
        }
      }
    }
  }
}

GET series/movie/_search?q=title:star

GET series/movie/_search?q=+year:>2010+title:trek

GET movies/movie/_search
{
  "query":{
    "match_all":{}
  },
  "from": 3, 
  "size":2
}

GET movies/movie/_search
{
  "query":{
    "match":{
      "title":"star"
    }
  }
}

GET movies/movie/_search
{
  "query":{
    "match":{
      "genre":"fantasy"
    }
  }
}

GET movies/movie/_search
{
  "query":{
    "bool":{
      "must":{
        "term":{
          "title":"star"
        }
      },
      "filter":{
        "range": {
          "year": {
            "gte": 2008
          }
        }
      }
    }
  }
}

GET movies/movie/_search
{
  "query":{
    "bool": {
      "must":{
        "term":{
          "title":"trek"
          }
        },
        "filter":{
          "range":{
            "year":{
              "gte":2010
            }
          }
        }
    }
  }
}

GET movies/movie/_search
{
  "query":{
    "bool":{
      "must":{
        "term":{
          "title":"star"
        }
      },
      "filter": {
        "terms":{
          "genre":[
              "action",
              "sci-fi"
            ]
        }
      }
    }
  }
}

GET movies/movie/_search
{
  "query": {
    "match": {
      "title": "star"
    }
  }
}

GET movies/movie/_search
{
  "query": {
    "match_all": {}
  }
}

GET movies/movie/_search
{
  "query": {
    "match_phrase": {
      "title": "from outer"
    }
  }
}

GET movies/movie/_search
{
  "query": {
    "match_phrase": {
      "title": {
        "query":"star beyond",
        "slop":1
      }
    } 
  }
}   

GET movies/movie/_search
{
  "query": {
    "bool":{
      "must":{
        "match_phrase":{
          "title":"star trek" 
        }
      },
      "filter":{
        "range":{
          "year":{
            "gte":1970
          }
        }
      }
    }
  }
}


GET movies/movie/_search 
{
  "query":{
    "bool":{
      "must":{
        "match_phrase":{
          "title":"star wars" 
        }
      },
      "filter": {
        "range":{
          "year":{
            "gt":1980 
          }
        }
      }
    }
  }
}

GET _cat/indices

GET movies/movie/_search?size=2&from=2

GET movies/movie/_search
{
  "from":0,
  "size":2,
  "query":{
    "match": {
      "genre": "drama"
    } 
  }
}

GET movies/movie/_search?sort=year

#DELETE movies



PUT movies
{
  "mappings": {
    "movie":{
      "properties": {
        "title":{
          "type":"text",
          "fields":{
            "raw":{
              "type":"keyword"
            }
          }
        }
      }
    }
  }
}



GET movies/movie/_search?sort=title.raw

GET movies/movie/_search
{
  "query":{
    "bool":{
      "must":{
        "match":{
          "genre":"sci-fi" 
        }
      },
      "must_not": {
        "match":{
          "title":"trek" 
        }
      },
      "filter": {
        "range": {
          "year": {
            "gte":2010, "lt":2015
          }
        }
      }
    }
  }
}

GET movies/movie/_search
{
  "sort": [
    {
      "title.raw": {
        "order": "asc"
      }
    }
  ],
  "query":{
    "bool":{
      "must":{
        "match":{
          "genre":"sci-fi" 
        }
      },
      "filter": {
        "range":{
          "year":{
            "lt":2015
          }
        }
      }
    }
  }
}

GET movies/movie/_search
{
  "query":{
    "fuzzy":{
      "title":{
        "value":"intrsteller", 
        "fuzziness": 2
      }
    }
  }
}

GET movies/movie/_search
{
  "query":{
    "match_phrase_prefix": {
      "title": {
        "query":"star trek",
        "slop":10
      }
    }
  }
}
#step 0
#DELETE movies
#step1
#create an autocomplete analyzer
PUT movies
{
  "settings": {
    "analysis": {
      "filter": {
        "autocomplete_filter":{
          "type":"edge_ngram",
          "min_gram":1,
          "max_gram":20
        }
      },
      "analyzer": {
        "autocomplete":{
          "type":"custom",
          "tokenizer":"standard",
          "filter":[
              "lowercase",
              "autocomplete_filter"
            ]
        }
      }
    }
  }
}
# to test above one
GET movies/_analyze
{
  "analyzer":"autocomplete",
  "text": "sta"
}

#step2
#now map your field with it
PUT movies/_mapping/movie
{
  "movie":{
    "properties": {
      "title":{
        "type":"text",
        "analyzer": "autocomplete"
      }
    }
  }
}

#step3
#but only use n-grams on the index side
#otherwize our query will also get split #into n-grams, and we'll get results for 
#everthing that matches 's','t','a','st'
#,etc
GET movies/movie/_search
{
  "query":{
    "match":{
      "title":{
        "query":"sta",
        "analyzer":"standard"
      }
    }
  }
}

GET shakespeare/_search
{
  
  "query":{
    "match_phrase": {
      "title": "star wars"
    }
  },
  "aggs":{
    "titles":{
      "terms":{
        "field": "title.raw"
      },
      "aggs": {
        "avg_rating": {
          "avg": {"field": "rating"}
        }
      }
    }
  }
}




