https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-scrolling.html





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

**********************************

cd /usr/share/elasticsearch

sudo bin/elasticsearch-plugin install x-pack

sudo vi /etc/elasticsearch/elasticsearch.yml
(Add xpack.security.enabled:false)

sudo /bin/systemctl stop elasticsearch.service

sudo /bin/systemctl start elasticsearch.service

cd /usr/share/kibana
sudo -u kibana bin/kibana-plugin install x-pack

sudo /bin/systemctl stop kibana.service

sudo /bin/systemctl start kibana.service


*************
if x pack is installed

username: elastic
password: changeme

*******************************

https://www.elastic.co/guide/en/elasticsearch/reference/current/cat.html

GET .watcher-history*/_search?pretty
{
  "sort":[
      {
        "result.execution_time":"desc"
      }
    ]
}
*******************************************

create a repository

add it into elasticsearch.yml

path.repo;["/home/<user>/backups"]

PUT _snapshot/backup-repo
{
	"type":"fs",
	"settings":{
		"location":"home/<user>/backups/backup-repo"
	}
}


snapshot all open indices:

PUT -snapshot/backup-repo/snapshot-1

get information about a snapshot

GET _snapshot/backup-repo/snapshot-1

monitor snapshot progress

GET _snapshot/backup-repo/snapshot-1/_status

restore a snapshot all indices

POST /_all/_close

POST _snapsot/backup-repo/snapshot-1/_restore
