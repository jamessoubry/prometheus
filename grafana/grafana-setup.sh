#!/bin/sh
set +e

echo "Provisioning Grafana with datasource and dashboards"
grafana_url=http://grafana:3000
password='foobar'
curl --fail -u admin:$password -X POST -H "Content-Type: application/json"  -d @- $grafana_url/api/datasources <<EOF
{
  "name":"Prometheus",
  "type":"prometheus",
  "url":"http://prometheus:9090",
  "Access":"proxy",
  "basicAuth":false
}
EOF

echo
dashboards='/grafana/dashboards/*'
for dashboard in `ls $dashboards`
do
  echo "Adding dashboard $dashboard"
  bodyjson=`cat $dashboard`
  curl --fail -u admin:$password -X POST -H "Content-Type: application/json" -d '{ "dashboard": '"$bodyjson"' , "overwrite": true }' $grafana_url/api/dashboards/db
  echo
done