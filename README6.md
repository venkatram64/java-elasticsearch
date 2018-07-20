POST /_aliases
{
	"actions":[
		{
			"add":{"alias": "logs_current","index":"logs_2017_06"}
		},
		{
			"remove":{"alias": "logs_current","index":"logs_2017_05"}
		},
		{
			"add":{"alias": "logs_last_3_months","index":"logs_2017_06"}
		},
		{
			"remove":{"alias": "logs_last_3_months","index":"logs_2017_03"}
		}
	]
}

PUT /testindex
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  }
}

GET /testindex/_settings

GET shakespeare/_settings

GET _search?size=0
{
  "query":{
    "match_phrase":{
      "title": "Star Wars"
    }
  },
  "aggs":{
    "title":{
      "terms":{
        "field": "title.raw"
      },
      "aggs": {
        "avg_rating": {
          "avg": {"field":"rating"}
        }
      }
    }
  }
}
