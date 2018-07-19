sudo apt-get install kibana

sudo vi /etc/kibana/kibana/yml

changer server.host to 0.0.0.0

sudo /bin/systemctl daemon-reload
sudo /bin/systemctl start kibana.service

kibana is now available on port 5601

if errors then run following command

sudo apt-get upgrade

http://127.0.0.1:5601

*********************

sudo apt-get update && sudo apt-get install filebeat

cd /usr/share/elasticsearch
sudo bin/elasticsearch-plugin install ingest-geoip
sudo bin/elasticsearch-plugin install ingest-user-agent
sudo /bin/systemctl stop elasticsearch.service
sudo /bin/systemctl start elasticsearch.servcie

cd /usr/share/filebeat/bin
sudo filebeat setup --dashboards

sudo vi /etc/filebeat/modules.d/apache2.yml

change access and error log paths to

["/home/<username>/logs/access*"]
["/home/<username>/logs/error*"]

Make /home/<username>/logs

cd into it

wget http://media.sundog-soft.com/es/access_log
sudo /bin/systemctl start filebeat.service
