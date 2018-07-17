Loading data from code

sudo apt install unzip

wget http://files.grouplens.org/datasets/movielens/ml-latest-small.zip

unzip ml-latest-small.zip

wget media.sundog-soft.com/es/MoviesToJson.py


python3 MoviesToJson.py > moremovies.json

curl -XDELETE 127.0.0.1:9200/movies

curl -XPUT 127.0.0.1:9200/_bulk --data-binary @moremovies.json

curl -XGET 127.0.0.1:9200/movies/_search?pretty

****************************

elastic search client libraries

sudo apt install python3-pip

sudo pip3 install elasticsearch

wget http://media.sundog-soft.com/es/IndexRatings.py

python3 IndexRatings.py

*******************
wget http://media.sundog-soft.com/es/IndexTags.py

python3 IndexTags.py

*********************

sudo apt-get update

sudo apt-get install logstash

wget media.sundog-soft.com/es/access_log

sudo vi /etc/logstash/conf.d/logstash.conf

input {
	file {
		path=>"/home/venkat/access_log"
		start_position => "beginning"
		ignore_older => 0
	}
}

filter {
	grok{
		match =>{ "message" => "%{COMBINEDAPACHELOG}"}
	}
	date {
			match=>["timestamp", "dd/MMM/yyyy:HH:mm:ss Z"]
	}
}

output {
	elasticsearch {
		hosts => ["localhost:9200"]
	}
	stdout {
		codec => rubydebug
	}
}

*******************
cd /usr/share/logstash/

sudo bin/logstash -f /etc/logstash/conf.d/logstash.conf

***************

list of indices

curl -XGET 127.0.0.1:9200/_cat/indices?v


