curl -XGET -S 'http://localhost:9200/_analyze?analyzer=whitespace&pretty=true' -d 'Red-orange gerbils live at #43A Franklin St.'

'query':{
	'match_all':{} # select * from events;
}


'query':{
	'term':{
		'city': 'Nashville'
	} 
}

'query':{
	'match':{
		'name': 'sort filbert'
	} 
}

'query':{
	'match_phrase':{
		'name': 'sorting filbert'
	} 
}

'query':{
	'match_phrase':{
		'name': 'filbert fun', 'slop': 2 # loose phrase
	} 
}

# complex queries with bool

'query':{
	'bool':{
		'must':[
			{
				'term':{
					'city': 'Nashville'
				}
			}
		],
		'should':[
			{
				'match_phrase':{
					'name':{
						'query': 'filbert fun',
						'slop':2,
						'boost': 2
					}
				}
			}
		]
	}
}

************************

'query':{
	'match_all':{}

},
'aggs':{
	'by_city':{
		'terms':{
			'field':'city'
		}
	},
	'by_price':{
		'histogram':{
			'field': 'price',
			'interval':10
		}
	}
}

************

'query':{
	'match_all':{}

},
'aggs':{
	'by_city':{
		'terms':{
			'field':'city'
		}
	},
	'aggs':{
		'by_price':{
			'histogram':{
				'field': 'price',
				'interval':10
			}
		}
	}
}

*********
'query':{
	'query_string':{
		'query':'tacos'
	}
}

************

'query':{
	'query_string':{
		'query':'tacos',
		'fields':['tags']
	}
}

***************************

'query':{
	'filtered':{
		'filter':{
			'range':{
				'rating':{
					'gte':4.0
				}
			}
		},
		'query':{
			'query_string':{
				'query':'tacos',
				'fields':['tags']
			}
		}
	}
}

****************************

'query':{
	'filtered':{
		'filter':{
			'range':{
				'rating':{
					'gte':4.0
				}
			}
		}
	}
}

******************************

'query':{
	'filtered':{
		'query':{
			'match':{
				'address.state':'ny'
			}
		},
		'filter':{
			'range':{
				'rating':{
					'gte':4.0
				}
			}
		}
	}
}

****************

curl -XGET localhost:9200/?pretty

GET /_search?size=5&from=0

GET /_search?size=5&from=10

GET /_search?q=name:john

GET /_search?q=date:2018-06-05

GET /myapp/tweet/_mapping

add new fields

can not change fields

delete and recreate index

PUT /myapp/tweet/_mapping -d '
{
	'tweet':{
		'properties':{
			....
		}
	}
}
'

full body search

GET /_search -d '
{
	'query':{
		'match_all':{}
	},
	'from': 0,
	'size':10
}
'

Filters:
exact matching
binary yes / no
fast
cacheable

Queries:
full text search
relevance scoring
heavier
not cacheable

combine filter & query

GET /_search -d '
{
	'query':{
		'filtered':{
			'query':{
				'match':{
					'tweet': 'search'
				}
			},
			'filter':{
				'term':{
					'nick':'@venkat'
				}
			}
		}
	}
}
'

******************



GET /_search -d '
{
	'query':{
		'filtered':{
			'query':{
				'match_all':{}
			},
			'filter':{
				'term':{
					'nick':'@venkat'
				}
			}
		}
	}
}
'

**************************

GET /_search -d '
{
	'query':{
		'filtered':{
			'filter':{
				'term':{
					'nick':'@venkat'
				}
			}
		}
	}
}
'

******************************

GET /_search -d '
{
	'query':{
		'filtered':{
			'filter':{
				'term':{
					'nick':'@venkat'
				}
			}
		}
	},
	'sort':{'date':'desc'}
}
'

***********************************
tweets for last month

GET /_search -d '
{
	'query':{
		'filtered':{
			'filter':{
				'range':{
					'date':{
						'gte':'2013-05-01',
						'lt': '2013-06-01'
					}
				}
			}
		}
	}
}
'

***********************************
top tweeters for query about elastic search

GET /_all/tweet/_search -d '
{
	'facets':{
		'top_tweets':{
			'terms':{
				'field':'nick'
			}
		}
	},
	'query':{
		'match':{
			'tweet': 'elasticsearch'
		}
	}
}'

***********************************
tweets by month

GET /_all/tweet/_search -d '
{
	'facets':{
		'tweets_by_month':{
			'date_histogram':{
				'field':'date',
				'interval': 'month'
			}
		}
	}
}'

*******************

edge N-Gram token filter

{
	'filter':{
		'autocomplete':{
			'type': 'edge_ngram',
			'min_gram':1,
			'max_gram':20
		}
	}
}

name field analyzers

{
	'analyzer':{
		'name':{
			'type': 'standard',
			'stopwords:[]
		},
		'name_autocomplete':{
			'type': 'custom',
			'tokenizer': 'standard',
			'filter':['lowercase', 'autocomplete']
		}
	}
}

name field mapping

{
	'name':{
		'type':'multi_field',
		'fields':{
			'name':{
				'type': 'string',
				'analyzer': 'name'
			},
			'autocomplete':{
				'type': 'string',
				'index_analyzer': 'name_autocomplete',
				'search_analyzer': 'name'
			}
		}
	}
}

****************

autocomplete query

{
	'bool':{
		'must':[{}],
		'must_not':[{}],
		'should':[{}]
	}
}
**************

autocomplete query

{
	'bool':{
		'must':{
			'match':{
				'name.autocomplete': 'john smi'
			}
		},
		'should':{
			'match': {
				'name': 'john smi'
			}
		}
	}
}

****************
sudo apt-get install defulat-jdk


wget -qO - https://artifacts.elasticco/GPG-KEY-elasticsearch | sudo apt-key add - 

sudo apt-get install apt-transport-https


echo "deb https://artifacts.elastic.co/packages/6.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-6.x.list

sudo apt-get update && apt-get install elasticsearch

vi /etc/elasticsearch/elasticsearch.yml


sudo /bin/systemctl start elasticsearch.service

curl 127.0.0.1:9200

wget http://media.sundog-soft.com/es6/shakes-mapping.json

curl -H "Content-Type: application/json" -XPUT 127.0.0.1:9200/shakespeare --data-binary @shakes-mapping.json

wget http://media.sundog-soft.com/es6/shakespeare_6.0.json

curl -H 'Content-Type: application/json' -XPOST 'localhost:9200/shakespeare/doc/_bulk?pretty' --data-binary @shakespeare_6.0.json

http://localhost:9200/shakespeare/_search?pretty

curl -H "Content-Type: application/json" -XGET '127.0.0.1:9200/shakespeare/_search?pretty' -d '
{
	"query":{
		"match_phrase":{
			"text_entry": "to be or not to be"
		}
	}
}
'
******************************
sudo apt-get install openssh-server

https://grouplens.org/datasets/movielens/

curl -H "Content-Type: application/json" -XPUT 127.0.0.1:9200/movies -d '
{
	"mappings":{
		"movie":{
			"properties":{
				"year":{
					"type":"date"
				}
			}
		}
	}
}
'

****************

curl -H "Content-Type: application/json" -XGET 127.0.0.1:9200/movies/_mapping/movie?pretty

curl -XPUT 127.0.0.0.1:9200/movies/movie/109487 -d '
	{
		"genere": ["IMAX","Sci-Fi"],
		"title": "Intersteller",
		"year": 2014
	}
'

****************
curl -XGET 127.0.0.1:9200/movies/movie/_search?pretty


wget http://media.sundog-soft.com/es/movies.json

curl -XPUT 127.0.0.1:9200/_bulk?pretty --data-binary @movies.json

***************

curl -XPOST 127.0.0.1:9200/movies/movie/109487/_update -d '
{
	"doc":{
		"title":"Intersteller woo"
	}
}
'

curl -XDELETE 127.0.0.1:9200/movies/movie/58559

curl -XGET 127.0.0.1:9200/movies/_search?pretty&q=Dark

***************************CRUD************

curl -XPUT 127.0.0.1:9200/movies/movie/2000?pretty -d '
{
	"title":"Little master",
	"genere":["Documentory"],
	"year":2018
}
'

**********************
curl -XGET 127.0.0.1:9200/movies/movie/2000?pretty

*****************************************
curl -XPOST 127.0.0.1:9200/movies/movie/2000/_update -d '
{
	"doc":{
		"genere":["Documentory","Comedy"]
	}
}
'

**********************
curl -XDELETE 127.0.0.1:9200/movies/movie/2000?pretty

********************UPDATE with version

curl -XPUT 127.0.0.1:9200/movies/movie/109487?version=3 -d '
{
	"genere":["IMAX","Sci-Fi"],
	"title": "Intersteller Foo",
	"year" : 2014
}
'

**************************

curl -XPOST 127.0.0.1:9200/movies/movie/109487/_update?retry_on_conflict=5 -d '
{
	"title": "Intersteller"
}
'

