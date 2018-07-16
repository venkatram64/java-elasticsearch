if you need to sort on an analyzed field, map an unanalyzed copy using the keyword type

curl -XPUT 127.0.0.1:9200/movies/ -d '
{
	"mappings":{
		"movie":{
			"properties":{
				"title":{
					"type": "text",
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
'

********************************


Now you can sort on the unanalyzed "raw" keyword field

curl -XGET '127.0.0.1:9200/movies/movie/_search?sort=title.raw&pretty'

curl -XGET '127.0.0.1:9200/movies/movie/_search?sort=year&pretty'

curl -XDELETE 127.0.0.1:9200/movies

************loading data ********************

curl -XPUT 127.0.0.1:9200/_bulk --data-binary @movies.json

curl -XGET 127.0.0.1:9200/movies/_search?pretty -d '
{
	"query":{
		"bool":{
			"must":{
				"match":{
					"genere":"Sci-Fi"
				}
			},
			"must_not":{
				"match":{
					"title": "trek"
				}
			},
			"filter":{
				"range": {
					"year":{
						"gte": 2010,
						"lt":2015
					}
				}
			}
		}
	}
}
'

*******************
curl -XGET 127.0.0.1:9200/movies/movie/_search?sort=title.raw&pretty' -d '
{
	"query":{
		"bool":{
			"must":{
				"match":{
					"genere":"Sci-Fi"
				}
			},
			"filter":{
				"range":{
					"year":{
						"lt":1960
					}
				}
			}
		}
	}
}
'

*****************fuzzy matches **********************
a way to account for typos and misspellings
the levenshtein edit distance accounts for:

substitutions:
insertions
deletion

curl -XGET 127.0.0.1:9200/movies/movie/_search?pretty -d '
{
	"query":{
		"fuzzy":{
			"title":{
				"value":"intrsteller","fuzziness":2
			}
		}
	}
}
'

**************partial query **************

curl -XGET 127.0.0.1:9200/movies/movie/_search?pretty -d '
{
	"query":{
		"prefix":{
			"year":"201"
		}
	}
}
'

**************wildcard query **************

curl -XGET 127.0.0.1:9200/movies/movie/_search?pretty -d '
{
	"query":{
		"wildcard":{
			"year":"1*"
		}
	}
}
'

