wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.42.zip

sudo apt-get install mysql-server

wget http://files.grouplens.org/datasets/movielens/ml-100k.zip

unzip ml-100k.zip


input{
	jdbc{
		jdbc_connection_string=>"jdbc:mysql://localhost;3306/movielens"
		jdbc_user=>"root"
		jdbc_password=>"password"
		jdbc_dirver_library=>"home/venkat/mysql-connector-java-5.1.42/mysql-connector-java-5.1.42-bin.jar"
		jdbc_driver_class=>"com.mysql.jdbc.Driver"
		statement=>"SELECT * FROM movies"
	}
}


************************

for kafka

sudo apt-get install zookeeperd

tar -xvf kafka_2.11-0.0.tgz

cd kafka_2.11-0.0.tgz
sudo bin/kafka-server-start.sh config/server.properties

sudo bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic kafka-logs

sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic kafka-logs < ../access_log

************************
cd /usr/share/logstash

sudo bin/logstash -f /etc/logstash/conf.d/logstash.conf


https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-20_2.11/6.3.1


./spark-2.3.1-bin-hadoop2.7/bin/spark-shell --packages org.elasticsearch:elasticsearch-spark-20_2.11:6.3.1


import org.elasticsearch.spark.sql._
case class Person(ID:Int, name:String, age:Int, numFriends:Int)

def mapper(line: String): Person = {
	val fields = line.split(',')
	val person:Person = Person(fields(0).toInt, fields(1), fields(2).toInt, fields(3).toInt)
	return person
}

import spark.implicits._

val lines = spark.sparkContext.textFile("fakefriends.csv")

val people = lines.map(mapper).toDF()

people.saveToEs("spark/people")
ctrl+d (get out of spark shell)

curl -XGET 127.0.0.1/spark/_search?pretty

******************************************

import org.elasticsearch.spark.sql._
case class Rating(userID:Int, movieID:Int, rating:Float, timestamp:Int)

def mapper(line:String):Rating = {
	val fields = line.split(',')
	val rating:Rating = Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toInt)
	return rating
}

import spark.implicits._
val lines = spark.sparkContext.textFile("ml-latest-small/ratings.csv")
val header = lines.first()
val data = lines.filter(row => row != header)
val ratings = data.map(mapper).toDF()
ratings.saveToEs("spark/ratings")

***************************
curl -XGET '127.0.0.1:9200/ratings/rating/_search?size=0&pretty' -d '
{
	"aggs":{
		"ratings":{
			"terms":{
				"field":"rating"
			}
		}
	}
}
'

curl -XGET '127.0.0.1:9200/ratings/rating/_search?size=0&pretty' -d '
{
	"query":{
		"match":{"rating":5.0}
	},
	"aggs":{
		"ratings":{
			"terms":{
				"field":"rating"
			}
		}
	}
}
'

*******************
curl -XGET '127.0.0.1:9200/ratings/rating/_search?size=0&pretty' -d '
{
	"query":{
		"match_phrase":{"rating":"Star Wars Episode IV"}
	},
	"aggs":{
		"avg_rating":{
			"avg":{
				"field":"rating"
			}
		}
	}
}
'
***********************
curl -XGET '127.0.0.1:9200/ratings/rating/_search?size=0&pretty' -d '
{
	"aggs":{
		"while_ratings":{
			"histogram":{
				"field":"rating",
				"interval":1.0
			}
		}
	}
}
'

*******************

curl -XGET '127.0.0.1:9200/movies/movie/_search?size=0&pretty' -d '
{
	"aggs":{
		"release":{
			"histogram":{
				"field":"year",
				"interval":10
			}
		}
	}
}
'

******************************
curl -XGET '127.0.0.1:9200/logstash-2017.05.01/_search?size=0&pretty' -d '
{
	"aggs":{
		"timestamp":{
			"date_histogram":{
				"field":"@timestamp",
				"interval":"hour"
			}
		}
	}
}
'

*************************************

when does google scrape me?

curl -XGET '127.0.0.1:9200/logstash-2017.05.01/_search?size=0&pretty' -d '
{
	"query":{
		"match":{
			"agent":"Googlebot"
		}
	},
	"aggs":{
		"timestamp":{
			"date_histogram":{
				"field":"@timestamp",
				"interval":"hour"
			}
		}
	}
}
'
******************************
curl -XGET '127.0.0.1:9200/logstash-2017.05.05/_search?size=0&pretty' -d '
{
	"query":{
		"match":{
			"response":500
		}
	},
	"aggs":{
		"timestamp":{
			"date_histogram":{
				"field":"@timestamp",
				"interval":"minute"
			}
		}
	}
}
'
********************************
curl -XGET '127.0.0.1:9200/ratings/rating/_search?size=0&pretty' -d '
{
	"query":{
		"match_phrase":{
			"title":"Star Wars"
		}
	},
	"aggs":{
		"titles":{
			"terms":{
				"field":"title"
			},
			"aggs":{
				"avg_rating":{
					"avg":{"field":"rating"}
				}
			}
		}
	}
}
'

above one gives error

so run below one and run above query

curl -XPUT '127.0.0.1:9200/ratings/_mapping/rating?pretty' -d '
{
	"properties":{
		"title":{
			"type": "text",
			"fielddata":true
		}
	}
}
'

****************************
did not work well so reindex the data

step 1:

curl -XDELETE 127.0.0.1:9200/ratings

step 2


curl -XPUT '127.0.0.1:9200/ratings' -d '
{
	"mappings":{
		"rating":{
			"properties":{
				"title":{
					"type":"text",
					"fielddata":true,
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
step 3:Load the data
python3 IndexRating.py


curl -XGET 127.0.0.1:9200/ratings/_mapping?pretty

curl -XGET '127.0.0.1:9200/ratings/rating/_search?size=0&pretty' -d '
{
	"query":{
		"match_phrase":{
			"title":"Star Wars"
		}
	},
	"aggs":{
		"titles":{
			"terms":{
				"field":"title.raw"
			},
			"aggs":{
				"avg_rating":{
					"avg":{"field":"rating"}
				}
			}
		}
	}
}
'
